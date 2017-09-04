package com.tc.test.resign.support;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tc.test.resign.service.impl.ResignServiceFactory;
import com.tc.test.resign.service.impl.ResignServiceImpl;
import com.tc.test.resign.thread.Consumers;

/**
 * Created by cai.tian on 2017/8/31.
 */
public class ConsumersAdapter {
    private final Logger logger = LoggerFactory.getLogger(ConsumersAdapter.class);

    private ResignServiceImpl resignServiceImpl;

    public ConsumersAdapter(){
        this.resignServiceImpl= ResignServiceFactory.get();
    }

    //同步
    public void receiveSync(final CountDownLatch latch, Integer receiveThreadNum) throws Exception {
        if (receiveThreadNum == null || receiveThreadNum == 0) return;
        final List<Consumers> clists = new ArrayList<>();
        for (int i = 0; i < receiveThreadNum; i++) {
            try {
                Consumers consumers = new Consumers("consumers" + i);
                clists.add(consumers);
            } catch (Exception e) {
                logger.info("consumers" + i + " 创建失败，{}", e);
            }
            clists.get(i).run();
        }
        //定时器查看数据中所有数据的状态
        final ScheduledExecutorService excutor = Executors.newSingleThreadScheduledExecutor();
        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();
        final Long l = System.nanoTime();

        excutor.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        lock.lock();
                        try {
                            System.out.println("---发送者数量---" + latch.getCount());
                            System.out.println(Thread.currentThread().getName() + "---定时器执行---");
                            if (System.nanoTime() - l > Constants.CONSUMERS_TIMEOUT) { //超时时间
                                //junit 测试用的
                                ResignUtils.to();
                                condition.signal();
                                excutor.shutdown();
                                System.out.println("---超时 定时器自己关闭自己---");
                            }
                            //excutor.awaitTermination()
                            boolean count = ResignUtils.count("0");
                            if (latch.getCount() == 0 && !count) {
                                try {
                                    ResignUtils.to();
                                } catch (Exception e) {
                                    logger.info("修改数据异常 ,{}", e);
                                }
                                consumersClose(clists);
                                RedisUtils.saveAndUpdateRedisStatus(Constants.MISSION_STOP);
                                excutor.shutdown();
                                System.out.println("---任务执行完 自动关闭---");
                            } else {
                                System.out.println("---还有没处理的任务 继续等待consumers---");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            lock.unlock();
                        }
                    }
                }, 5, 5, TimeUnit.SECONDS //计时单位
        );
        //junit 测试用的
        lock.lock();
        System.out.println("--- 阻塞jvm ---");
        condition.await();
        System.out.println("--- 锁已释放 jvm 关闭 ---");
    }

    //异步
    public void receiveAsync(final CountDownLatch latch, Integer receiveThreadNum) throws Exception {
        if (receiveThreadNum == null || receiveThreadNum == 0) return;
        final List<Consumers> clists = new ArrayList<>();
        for (int i = 0; i < receiveThreadNum; i++) {
            try {
                Consumers consumers = new Consumers("consumers" + i);
                clists.add(consumers);
            } catch (Exception e) {
                logger.info("consumers" + i + " 创建失败，{}", e);
            }
            clists.get(i).run();
        }
        //定时器查看数据中所有数据的状态
        final ScheduledExecutorService excutor = Executors.newSingleThreadScheduledExecutor();
        final Long l = System.nanoTime();
        excutor.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("---发送者数量---" + latch.getCount());
                            System.out.println(Thread.currentThread().getName() + "---定时器执行---");
                            if (System.nanoTime() - l > Constants.CONSUMERS_TIMEOUT) { //超时时间
                                //junit 测试用的
                                ResignUtils.to();
                                excutor.shutdown();
                                System.out.println("---超时 定时器自己关闭自己---");
                                return;
                            }
                            //excutor.awaitTermination()
                            boolean count = ResignUtils.count("0");
                            if (latch.getCount() == 0 && !count) {
                                try {
                                    ResignUtils.to();
                                } catch (Exception e) {
                                    logger.info("修改数据异常 ,{}", e);
                                }
                                consumersClose(clists);
                                RedisUtils.saveAndUpdateRedisStatus(Constants.MISSION_STOP);
                                excutor.shutdown();
                                System.out.println("---任务执行完 自动关闭---");
                            } else {
                                System.out.println("---还有没处理的任务 继续等待consumers---");
                            }
                        } catch (Exception e) {
                            logger.info("监控消费者状态的定时器出现异常 ,{}", e);
                        }
                    }
                }, 5, 5, TimeUnit.SECONDS //计时单位
        );
    }



    private void consumersClose(List<Consumers> clists){
        for (Consumers c : clists) {
            try {
                c.close();
            } catch (Exception e) {
                logger.info(c.getConsumerName() + " 关闭连接出现异常 ,{}", e);
            }
        }
    }



}

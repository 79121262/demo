package com.tc.test.resign.support;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tc.test.resign.thread.Producer;

/**
 * Created by cai.tian on 2017/8/31.
 */
public class ProducerAdapter {
    private final Logger logger = LoggerFactory.getLogger(ProducerAdapter.class);

    //同步
    public void sendSync(List<Queue<String>> queues) throws Exception {
        if (queues.isEmpty()) return;
        final CountDownLatch latch = new CountDownLatch(queues.size());
        final List<Producer> producer = new ArrayList<>();
        final ExecutorService service = Executors.newFixedThreadPool(queues.size());
        for (int i = 0; i < queues.size(); i++) {
            Producer s = new Producer(queues.get(i), latch, "send " + i);
            service.submit(s);
            producer.add(s);
        }
        latch.await();
        System.out.println("发送完毕 生产者连接关闭");
        for (Producer s : producer) {
            try {
                s.close();
            } catch (Exception e) {
                logger.info("关闭生产者连接失败 {}", e);
            }
        }
        service.shutdown();
    }

    //异步
    public CountDownLatch sendAsync(List<Queue<String>> queues) throws Exception {
        if (queues == null || queues.isEmpty()) return null;
        final CountDownLatch latch = new CountDownLatch(queues.size());
        final List<Producer> producer = new ArrayList<>();
        final ExecutorService service = Executors.newFixedThreadPool(queues.size());
        for (int i = 0; i < queues.size(); i++) {
            Producer s = new Producer(queues.get(i), latch, "send " + i);
            service.submit(s);
            producer.add(s);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    System.out.println("发送完毕 生产者连接关闭");
                    for (Producer s : producer) {
                        try {
                            s.close();
                        } catch (Exception e) {
                            logger.info("关闭生产者连接失败 {}", e);
                        }
                    }
                    service.shutdown();
                } catch (Exception e) {
                    System.out.println("生产者 观察线程失败");
                }
            }
        }).start();
        return latch;
    }
}

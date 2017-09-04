package com.tc.test.resign.thread;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.tc.test.resign.service.impl.RabbitMqConfigServiceImpl;
import com.tc.test.resign.service.impl.ResignServiceFactory;
import com.tc.test.resign.support.ResignUtils;

/**
 * Created by cai.tian on 2017/8/31.
 */
public class Producer implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Consumers.class);
    private Queue<String> queue;
    private String sendName;
    private Connection connection = null;
    private Channel channel = null;
    private CountDownLatch countDownLatch;
    private RabbitMqConfigServiceImpl config;

    public Producer(Queue<String> queue, CountDownLatch countDownLatch, final String sendName) throws Exception {
        this.config = ResignServiceFactory.get().getRabbitMqConfigServiceImpl();
        this.queue = queue;
        this.sendName = sendName;
        this.countDownLatch = countDownLatch;
        this.connection = config.getRabbitMqConnection();
        this.channel = this.connection.createChannel();
        this.channel.queueDeclare(config.getQueueName(), false, false, false, null);
        this.channel.exchangeDeclare(config.getExchangeName(), "topic");
        this.channel.queueBind(config.getQueueName(), config.getExchangeName(), config.getBindingKey());
        this.connection.addShutdownListener(new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException cause) {
                System.out.println(sendName + ":关闭");
            }
        });
    }

    @Override
    public void run() {
        try {
            if (queue != null)
                while (!queue.isEmpty()) {
                    String value = queue.poll();
                    channel.txSelect();
                    channel.basicPublish(config.getExchangeName(), config.getBindingKey(), null, (sendName + ":" + value).getBytes());
                    try {
                        ResignUtils.insertDb(sendName + ":" + value, "0");
                        channel.txCommit();
                    } catch (Exception e) {
                        channel.txRollback();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        countDownLatch.countDown();
    }

    public void close() throws Exception {
        channel.close();
        connection.close();
    }
}

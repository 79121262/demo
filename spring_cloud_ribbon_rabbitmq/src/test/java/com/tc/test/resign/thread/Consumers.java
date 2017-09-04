package com.tc.test.resign.thread;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.tc.test.resign.service.impl.RabbitMqConfigServiceImpl;
import com.tc.test.resign.service.impl.ResignServiceFactory;
import com.tc.test.resign.support.ResignUtils;

/**
 * Created by cai.tian on 2017/8/31.
 */
public class Consumers {
    private final Logger logger = LoggerFactory.getLogger(Consumers.class);
    private Connection connection = null;
    private Channel channel = null;
    private QueueingConsumer consumer = null;
    private Object flag = new Object();
    private String consumerName = null;
    private RabbitMqConfigServiceImpl config;

    public Consumers(final String consumerName) throws Exception {
        this.config = ResignServiceFactory.get().getRabbitMqConfigServiceImpl();
        this.connection = config.getRabbitMqConnection();
        this.channel = this.connection.createChannel();
        this.channel.basicQos(5); //每次只接受几个
        this.consumerName = consumerName;
        this.channel.queueDeclare(config.getQueueName(), false, false, false, null); //保持一致
        this.channel.exchangeDeclare(config.getExchangeName(), "topic");
        this.channel.queueBind(config.getQueueName(), config.getExchangeName(), config.getBindingKey());
        this.connection.addShutdownListener(new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException cause) {
                System.out.println(consumerName + ":关闭");
            }
        });
    }

    public void close() throws Exception {
        synchronized (flag) {
            channel.close();
            connection.close();
        }
    }

    public void run() {
        try {
            consumer = new QueueingConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    synchronized (flag) {
                        if (channel.isOpen()) {
                            String message = new String(body, "UTF-8");
                            //System.out.println(consumerName + ":" + Thread.currentThread().getName() + ":" + consumerTag + " [x] Received '" + message + "'");
                            try {
                                //修改数据库为最终状态
                                ResignUtils.updateDb(message, "1");

                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.info(consumerName + " 消费者修改 数据异常 ,{}", e);
                            } finally {
                                channel.basicAck(envelope.getDeliveryTag(), true); //出队
                            }
                        }
                    }
                }
            };
            //consumer.nextDelivery();
            channel.basicConsume(config.getQueueName(), false, consumer);
        } catch (Exception e) {
            logger.info(consumerName + " 启动失败 ,{}", e);
            e.printStackTrace();
            try {
                close();
            } catch (Exception e1) {
                logger.info(consumerName + " 连接关闭异常 ,{}", e);
            }
        }
    }

    public String getConsumerName() {
        return consumerName;
    }
}

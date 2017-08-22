package com.tc.consume;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者自动应答模式
 * @author cai.tian
 */
@Component
@RabbitListener(queues = "tc-queues",containerFactory = "uContainerFactory")
public class Receiver {
    @RabbitHandler
    public void process(Object hello) {
        System.out.println("Receiver : " + hello);
    }
}
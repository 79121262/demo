package com.tc.produce;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    public void send() {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        //this.rabbitTemplate.convertAndSend("tc-queues", context); //发送到默认 exchange
        this.rabbitTemplate.convertAndSend("test-exchange","test-routingkey", context);
    }
}
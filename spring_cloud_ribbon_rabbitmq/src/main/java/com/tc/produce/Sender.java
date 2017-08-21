package com.tc.produce;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private Boolean t=true;
    
    //生成者  rabbit回调 确认 是否发送成功
    public void send() {
    	if(t){
    		rabbitTemplate.setConfirmCallback(new ConfirmCallback() { 
    			
    			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    				if(ack){
    					//correlationData.toString()
                        System.out.println("成功+:"+cause);
    				}else{
    					System.out.println("失败+:"+cause);
    				}
    			}
    		});
    		t=false;
    	}
    	
    	
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        //this.rabbitTemplate.convertAndSend("tc-queues", context); //发送到默认 exchange
        this.rabbitTemplate.convertAndSend("test-exchange","test-routingkey", context);
    }
}
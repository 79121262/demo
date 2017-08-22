package com.tc.produce;

import java.util.Date;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactoryUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

@Component
public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private int i=0;
    
    //生成者  rabbit回调 确认 是否发送成功
    public void send() throws Exception {
    	String context = "hello " + new Date();
        System.out.println("Sender : " + context);
    	this.rabbitTemplate.convertAndSend("test-exchange","test-routingkey", context);

    }
    //确认模式
    public void sendConfirm() throws Exception {
    	//connectionFactory.setPublisherConfirms(true); //必须要设置  开启应答 和事物冲突
		rabbitTemplate.setConfirmCallback(new ConfirmCallback() { 
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if(ack){
                    System.out.println("成功+:"+cause);
				}else{
					System.out.println("失败+:"+cause);
				}
			}
		});
    	String context = "hello " + new Date();
        System.out.println("Sender : " + context);
    	this.rabbitTemplate.convertAndSend("test-exchange","test-routingkey", context);
    }
    //事物模式
    public void sendTransactional() throws Exception {
    	//无需设置
    	//connectionFactory.setPublisherConfirms(true); 
        Channel channel = ConnectionFactoryUtils.getTransactionalResourceHolder(rabbitTemplate.getConnectionFactory(), true).getChannel();
        //开启事物
        channel.txSelect();
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        channel.basicPublish("test-exchange","test-routingkey",true,MessageProperties.PERSISTENT_BASIC,context.getBytes());
        if (++i%2 == 0) {//事物，偶数的回滚  
            //throw new RuntimeException("回滚事物");
        	channel.txRollback();
        	System.out.println("txRollback : ");
        }else{
            //提交事务  
            channel.txCommit();  
            System.out.println("txCommit : ");
        }
    }
}
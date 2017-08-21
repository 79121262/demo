package com.tc.consume;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
@Component
//@RabbitListener(queues = "tc-queues",containerFactory = "uContainerFactory")
public class TransactionConsumeImpl implements ChannelAwareMessageListener  {
//    @RabbitHandler
//    public void process(String hello) {
//        System.out.println("Receiver : " + hello);
//    }
//    
	 @Override
	 public void onMessage(Message message, Channel channel) throws Exception {
		 System.out.println("tc-queues Transaction : " + message);
		 System.out.println("tc-queues Transaction channel : " + channel);
		 //删除
		 //channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		 //重发
		 channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
		 
	 }
}
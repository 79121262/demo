package com.tc.consume;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/**
 * 消费者应答模式
 * @author cai.tian
 */
@Component
public class TransactionConsumeImpl implements ChannelAwareMessageListener  {
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
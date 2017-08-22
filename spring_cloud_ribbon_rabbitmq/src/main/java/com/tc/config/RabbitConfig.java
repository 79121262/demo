package com.tc.config;


import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.tc.consume.TransactionConsumeImpl;



@Configuration
public class RabbitConfig {
	/**
	 * 生成者消费者公用
	 * @return
	 */
	@Bean  
    public ConnectionFactory connectionFactory() {  
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();  
        connectionFactory.setAddresses("10.198.197.127");  
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("tc");  
        connectionFactory.setPassword("tc123456");  
        connectionFactory.setVirtualHost("/tc");  
        //connectionFactory.setPublisherConfirms(true); //必须要设置  开启应答 和事物冲突
        return connectionFactory;  
    }
	
	//生产者
	@Bean
    //** 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置 *//*  
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
    public RabbitTemplate rabbitTemplate1() {  
        RabbitTemplate template = new RabbitTemplate(connectionFactory());  
        //开启外部事物spring管理
        //template.setChannelTransacted(true);
        //注册转换器
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;  
    }
	
	/**
	 * 创建队列 生产者和消费者都可以创建
	 * @return
	 */
    @Bean
    public Queue helloQueue() {
        Queue queue = new Queue("tc-queues");
        return queue;
    }
    
    /**
	 * 创建队列 生产者和消费者都可以创建
	 * @return
	 */
    @Bean
    public Queue helloQueue1() {
        return new Queue("tc-queues1");
    }
    
    /**
	 * 创建exchange 生产者和消费者都可以创建
	 * @return
	 */
    @Bean
    public TopicExchange ats2cuTopic() {
       return new TopicExchange("test-exchange");
    }
    
    /**
  	 * 创建exchange 和队列关联
  	 * @return
  	 */
    @Bean
    Binding bindingExchangeMessage(Queue helloQueue, TopicExchange ats2cuTopic) {
        return BindingBuilder.bind(helloQueue).to(ats2cuTopic).with("test-routingkey");
    }
    /**
  	 * 创建exchange 和队列关联
  	 * @return
  	 */
    @Bean
    Binding bindingExchangeMessage1(Queue helloQueue1, TopicExchange ats2cuTopic) {
        return BindingBuilder.bind(helloQueue1).to(ats2cuTopic).with("test-routingkey");
    }
    
   /* @Bean
    SimpleMessageListenerContainer container(ConnectionFactory rabbitConnectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory);
        container.setQueueNames("tc-queues");
        container.setMessageListener(listenerAdapter);
        container.setMessageConverter(new Jackson2JsonMessageConverter());
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        MessageListenerAdapter m = new MessageListenerAdapter(receiver, "process");
        m.setMessageConverter(new Jackson2JsonMessageConverter());
        return m;
    }*/
    
    //客户端(消费者) 监听工厂
    @Bean(name="uContainerFactory")
    public SimpleRabbitListenerContainerFactory uRabbitListenerContainerFactory(
            @Qualifier("connectionFactory") ConnectionFactory connectionFactory,
            RabbitProperties config
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        RabbitProperties.Listener listenerConfig = config.getListener();
        factory.setAutoStartup(listenerConfig.isAutoStartup());
        //注册转换器
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        if (listenerConfig.getAcknowledgeMode() != null) {
            factory.setAcknowledgeMode(listenerConfig.getAcknowledgeMode());
        }
        if (listenerConfig.getConcurrency() != null) {
            factory.setConcurrentConsumers(listenerConfig.getConcurrency());
        }
        if (listenerConfig.getMaxConcurrency() != null) {
            factory.setMaxConcurrentConsumers(listenerConfig.getMaxConcurrency());
        }
        if (listenerConfig.getPrefetch() != null) {
            factory.setPrefetchCount(listenerConfig.getPrefetch());
        }
        if (listenerConfig.getTransactionSize() != null) {
            factory.setTxSize(listenerConfig.getTransactionSize());
        }
        return factory;
    }
    
    //支持手动确认的 listener
    @Bean
    public SimpleMessageListenerContainer messageContainer1(Queue helloQueue,ConnectionFactory connectionFactory, TransactionConsumeImpl receiver) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(helloQueue);
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        container.setMessageListener(receiver);
        return container;
    }
    
    
}
package com.tc.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig {
    @Bean
    public Queue helloQueue() {
        return new Queue("tc-queues");
    }
    
    
    @Bean
    public TopicExchange ats2cuTopic() {
       return new TopicExchange("test-exchange");
    }
    
    
    @Bean
    Binding bindingExchangeMessage(Queue helloQueue, TopicExchange ats2cuTopic) {
        return BindingBuilder.bind(helloQueue).to(ats2cuTopic).with("test-routingkey");
    }
    

    
}
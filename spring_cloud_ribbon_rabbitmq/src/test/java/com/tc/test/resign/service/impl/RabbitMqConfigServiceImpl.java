package com.tc.test.resign.service.impl;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tc.test.resign.service.RabbitMqConfigService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by cai.tian on 2017/8/31.
 */
@Service
public class RabbitMqConfigServiceImpl implements RabbitMqConfigService {
    private final Logger logger = LoggerFactory.getLogger(RabbitMqConfigServiceImpl.class);
    @Value("${portal.resign.rabbit.host}")
    private String host;

    @Value("${portal.resign.rabbit.uname}")
    private String userName;

    @Value("${portal.resign.rabbit.pwd}")
    private String password;

    @Value("${portal.resign.rabbit.port}")
    private String port;

    @Value("${portal.resign.rabbit.queue.name}")
    private String queueName;

    @Value("${portal.resign.rabbit.virtual.host}")
    private String virtualHost;

    private String exchangeName = "resign_exchange_name";
    private String bindingKey = "binding_key";

    public Connection getRabbitMqConnection() throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        // 指定用户 密码
        factory.setUsername(userName);
        factory.setPassword(password);
        // 指定端口
        factory.setPort(Integer.valueOf(port));
        factory.setVirtualHost(virtualHost);
        //创建一个新的连接
        Connection connection = factory.newConnection();
        return connection;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void setBindingKey(String bindingKey) {
        this.bindingKey = bindingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getBindingKey() {
        return bindingKey;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getHost() {
        return host;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getPort() {
        return port;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

}

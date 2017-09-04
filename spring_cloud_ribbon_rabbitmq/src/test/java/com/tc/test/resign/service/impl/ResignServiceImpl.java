package com.tc.test.resign.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tc.test.resign.service.ResignService;

/**
 * Created by cai.tian on 2017/9/1.
 */
@Service
public class ResignServiceImpl extends ResignService {

    @Autowired
    @Qualifier("redisTemplateResign")
    private RedisTemplate redisTemplateResign;

    @Autowired
    private RabbitMqConfigServiceImpl rabbitMqConfigServiceImpl;

    public RedisTemplate getRedisTemplateResign() {
        return redisTemplateResign;
    }

    public RabbitMqConfigServiceImpl getRabbitMqConfigServiceImpl() {
        return rabbitMqConfigServiceImpl;
    }
}

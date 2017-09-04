package com.tc.test.resign.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by cai.tian on 2017/8/31.
 */
@Service
public class ResignServiceFactory implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ResignServiceFactory.class);
    private static ApplicationContext applicationContexts;
    private static ResignServiceImpl resignService;

    public synchronized static ResignServiceImpl get() {
        if (resignService == null) {
            if (applicationContexts == null) {
                logger.info("spring ApplicationContext为null请检查 spring 环境");
                return null;
            }
            resignService = applicationContexts.getBean(ResignServiceImpl.class);
        }
        return resignService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContexts = applicationContext;
    }
}

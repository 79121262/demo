package com.tc.test.resign;


import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tc.test.resign.support.Constants;
import com.tc.test.resign.support.ConsumersAdapter;
import com.tc.test.resign.support.ProducerAdapter;
import com.tc.test.resign.support.RedisUtils;
import com.tc.test.resign.support.ResignUtils;

/**
 * Created by cai.tian on 2017/8/31.
 */
public class Engine {
    private static final Logger logger = LoggerFactory.getLogger(Engine.class);
    //Adap
    public void start() throws Exception {
        logger.info("判断redis中标记");
        if(Constants.MISSION_RUNING.equals(RedisUtils.getRunStatus())){
            logger.info("正在有任务在执行...");
            return;
        }
        Boolean aBoolean = RedisUtils.saveAndUpdateRedisStatus(Constants.MISSION_RUNING);
        if(!aBoolean){
            return ;
        }
        ProducerAdapter pa = new ProducerAdapter();
        final CountDownLatch countDownLatch = pa.sendAsync(ResignUtils.getQueues(3));
        ConsumersAdapter ca = new ConsumersAdapter();
        ca.receiveAsync(countDownLatch, 3);
       
    }
}

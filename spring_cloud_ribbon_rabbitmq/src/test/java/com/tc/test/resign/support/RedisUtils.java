package com.tc.test.resign.support;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.tc.test.resign.service.impl.ResignServiceFactory;

/**
 * Created by cai.tian on 2017/9/1.
 */
public class RedisUtils {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    public static Boolean saveAndUpdateRedisStatus(String status){
        RedisTemplate redisTemplateResign = ResignServiceFactory.get().getRedisTemplateResign();
        //ValueOperations<String,String> valueOperations = redisTemplateResign.opsForValue();
        try {
            logger.info("修改redis中任务执行的标记为 "+status);
            redisTemplateResign.execute(new RedisCallback<Boolean>() {  
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {  
                    RedisSerializer<String> serializer = redisTemplateResign.getStringSerializer();  
                    connection.set(serializer.serialize(Constants.IS_COMPLETE_MISSION_REDIS_KEY), serializer.serialize(status));  
                    return true;  
                }  
            }); 
            //valueOperations.set(Constants.IS_COMPLETE_MISSION_REDIS_KEY,status);
            return true;
        }catch (Exception e){
            logger.error("修改redis 标记失败了,errorMsg:{}", e);
            return false;
        }
    }

    public static String getRunStatus(){
        RedisTemplate redisTemplateResign = ResignServiceFactory.get().getRedisTemplateResign();
        //ValueOperations<String,String> valueOperations = redisTemplateResign.opsForValue();
        try {
            logger.info("获取redis执行状态");
            
            return (String) redisTemplateResign.execute(new RedisCallback<String>() {  
                public String doInRedis(RedisConnection connection) throws DataAccessException {  
                    RedisSerializer<String> serializer = redisTemplateResign.getStringSerializer();  
                    byte[] value =  connection.get(serializer.serialize(Constants.IS_COMPLETE_MISSION_REDIS_KEY));  
                    return serializer.deserialize(value);  
                }  
            }); 
            //return valueOperations.get(Constants.IS_COMPLETE_MISSION_REDIS_KEY);
        }catch (Exception e){
            logger.error("获取redis执行状态失败了,errorMsg:{}", e);
            return null;
        }
    }

}

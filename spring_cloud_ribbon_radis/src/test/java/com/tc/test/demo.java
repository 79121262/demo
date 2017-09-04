package com.tc.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.tc.example.Application;
import com.tc.service.IRedisService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class demo {
	  @Autowired  
	  private RedisTemplate<String, ?> redisTemplate;  
	  @Autowired  
	  private IRedisService redisService;  
	  
	  @Test
	  public void main(){
		  //Long increment = redisTemplate.opsForValue().increment("dsad", 1);
		  Object p=redisTemplate.execute(new RedisCallback<Object>() {  
	           public Object doInRedis(RedisConnection connection) throws DataAccessException {  
	                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
	                byte[] value =  connection.get(serializer.serialize("person"));  
	                return value;  
	           }  
	      });  
		  Gson gson=new Gson();
		  String json = gson.toJson(p);
		  System.out.println(json);
		  
		  redisService.set("java", "fad");
		  String string = redisService.get("name");
		  System.out.println(string);
	  }
	 
	
}

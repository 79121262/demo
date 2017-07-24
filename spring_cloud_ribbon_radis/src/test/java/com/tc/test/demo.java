package com.tc.test;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

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
		  String string = redisService.get("name");
		  System.out.println(string);
		
	  }
	  
	  //∑¥–Ú¡–ªØ
	    public static Object unserizlize(byte[] byt){
	        ObjectInputStream oii=null;
	        ByteArrayInputStream bis=null;
	        bis=new ByteArrayInputStream(byt);
	        try {
	            oii=new ObjectInputStream(bis);
	            Object obj=oii.readObject();
	            return obj;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }


	        return null;
	    }
	  
	  static class  Person implements Serializable{
	        String age;
	        String name;

	        public void setAge(String age) {
	            this.age = age;
	        }

	        public void setName(String name) {
	            this.name = name;
	        }

	        public String getAge() {

	            return age;
	        }

	        public String getName() {
	            return name;
	        }
	    }
	
}

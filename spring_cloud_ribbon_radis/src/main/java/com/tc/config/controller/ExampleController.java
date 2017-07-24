package com.tc.config.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tc.service.IRedisService;

@RestController
public class ExampleController {  
      
    @Autowired  
    private IRedisService redisService;  
      
    @RequestMapping("/users")
    public String users(){  
        return "";  
    }  
      
    @RequestMapping("/redis/set")  
    public String redisSet(@RequestParam("value")String value){  
        return "";
    }  
      
    @RequestMapping("/redis/get")  
    public String redisGet(){  
        String name = redisService.get("name");  
        return name;  
    }  
      
} 
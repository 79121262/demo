package com.tc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tc.service.ComputeService;

//post 提交  http://localhost:3333/refresh
@RefreshScope
@RestController
public class ComputeController {
	@Value("${hello}")
	private String helloConfig;
	
	@Autowired
	private ComputeService computeService;
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		return computeService.addService();
	}
	//获取配置中心配置
	@RequestMapping(value = "/getConfig", method = RequestMethod.GET)
	public String getConfig() {
		return helloConfig;
	}
	
}

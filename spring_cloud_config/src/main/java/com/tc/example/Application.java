package com.tc.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Created by yangyibo on 17/1/17.
 */

@EnableConfigServer
@SpringBootApplication
//注册服务
@EnableDiscoveryClient
public class Application {
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
		System.out.println("--配置中心1--");
	}
}

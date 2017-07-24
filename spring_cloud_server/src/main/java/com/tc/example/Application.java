package com.tc.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
/**
 * Created by yangyibo on 17/1/17.
 */

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages={"com.tc.controller"})
public class Application {
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
		System.out.println("--服务提供--");
	}

}

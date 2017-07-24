package com.tc.example;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.tc.example.filter.AccessFilter;

@EnableZuulProxy
@SpringCloudApplication
public class Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
		System.out.println("--服务网关--");
	}

	@Bean
	public AccessFilter accessFilter() {
		return new AccessFilter();
	}

}
package com.bazar.bazar_frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.bazar.bazar_frontend.load_balancer.LoadBalancerProps;

@SpringBootApplication
@EnableConfigurationProperties(LoadBalancerProps.class)
public class BazarFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BazarFrontendApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

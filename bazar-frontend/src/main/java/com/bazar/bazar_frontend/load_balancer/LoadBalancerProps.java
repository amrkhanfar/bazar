package com.bazar.bazar_frontend.load_balancer;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties("replicas")
@Getter
@Setter
public class LoadBalancerProps {
    private List<String> catalog;
    private List<String> order;
}
package com.bazar.bazar_frontend.load_balancer;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties("replicas")
@Getter
@Setter
public class LoadBalancerProps {
    private List<String> catalog;
    private List<String> order;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoadBalancerProps.class);

    @PostConstruct
    public void init(){
    log.info("Catalog replicas: {}", catalog);
    log.info("Order replicas: {}", order);
    }

}


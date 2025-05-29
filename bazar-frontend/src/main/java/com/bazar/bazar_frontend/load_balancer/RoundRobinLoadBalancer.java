package com.bazar.bazar_frontend.load_balancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Load balancer for stateless requests
 * Reads replica lists from application.yml at startup
 */
@Component
public class RoundRobinLoadBalancer {

    private final List<String> catalogUrls;
    private final List<String> orderUrls;
    private final AtomicInteger cIdx = new AtomicInteger(0);
    private final AtomicInteger oIdx = new AtomicInteger(0);
    
    @Autowired
    private LoadBalancerProps props;

    public RoundRobinLoadBalancer() {
        this.catalogUrls = props.getCatalog();
        this.orderUrls   = props.getOrder();
    }

    public String nextCatalog() {
        return catalogUrls.get(Math.abs(cIdx.getAndIncrement()) % catalogUrls.size());
    }
    public String nextOrder() {
        return orderUrls.get(Math.abs(oIdx.getAndIncrement()) % orderUrls.size());
    }
}


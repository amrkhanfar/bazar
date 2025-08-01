package com.bazar.bazar_frontend.load_balancer;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RoundRobinLoadBalancer {

    private final AtomicInteger catalogCounter = new AtomicInteger(0);
    private final AtomicInteger orderCounter = new AtomicInteger(0);

    private final List<String> catalogServers = List.of("http://bazar-catalog1-service:8081", "http://bazar-catalog2-service:8081");
    private final List<String> orderServers = List.of("http://bazar-order1-service:8082", "http://bazar-order2-service:8082");

    public String getCatalogServer() {
        return catalogServers.get(catalogCounter.getAndIncrement() % catalogServers.size());
    }

    public String getOrderServer() {
        return orderServers.get(orderCounter.getAndIncrement() % orderServers.size());
    }
}

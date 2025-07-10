package com.bazar.bazar_order.load_balancer;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RoundRobinLoadBalancer {

    private final AtomicInteger catalogCounter = new AtomicInteger(0);

    private final List<String> catalogServers = List.of("http://bazar-catalog1-service:8081", "http://bazar-catalog2-service:8081");

    public String getCatalogServer() {
        return catalogServers.get(catalogCounter.getAndIncrement() % catalogServers.size());
    }
}

package com.bazar.bazar_frontend.load_balancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class RoundRobinLoadBalancer {

    private final List<String> catalogUrls;
    private final List<String> orderUrls;
    private final AtomicInteger cIdx = new AtomicInteger(0);
    private final AtomicInteger oIdx = new AtomicInteger(0);

    public RoundRobinLoadBalancer(LoadBalancerProps props) {
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

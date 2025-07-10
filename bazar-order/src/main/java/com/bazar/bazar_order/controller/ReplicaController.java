package com.bazar.bazar_order.controller;

import com.bazar.bazar_order.model.Order;
import com.bazar.bazar_order.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/replica")
public class ReplicaController {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${replica.urls}")
    private List<String> replicaUrls;

    @Value("${replica.id}")
    private String replicaId;

    @PostMapping("/add")
    public void add(@RequestBody Order order) {
        orderRepo.save(order);
    }

    public void propagateAdd(Order order) {
        for (String url : replicaUrls) {
            if (!url.contains(replicaId)) { // Do not send to self
                restTemplate.postForObject(url + "/replica/add", order, Order.class);
            }
        }
    }
}

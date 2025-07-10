package com.bazar.bazar_order.service;

import com.bazar.bazar_order.controller.ReplicaController;
import com.bazar.bazar_order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class PropagationService {

    @Autowired
    private ReplicaController replicaController;

    @TransactionalEventListener
    public void propagateAdd(Order order) {
        replicaController.propagateAdd(order);
    }
}

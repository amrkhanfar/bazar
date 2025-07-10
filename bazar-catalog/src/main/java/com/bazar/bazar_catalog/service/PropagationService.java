package com.bazar.bazar_catalog.service;

import com.bazar.bazar_catalog.controller.ReplicaController;
import com.bazar.bazar_catalog.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class PropagationService {

    @Autowired
    private ReplicaController replicaController;

    @TransactionalEventListener
    public void propagateAdd(Book book) {
        replicaController.propagateAdd(book);
    }
}

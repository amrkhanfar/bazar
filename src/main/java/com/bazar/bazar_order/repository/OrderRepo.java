package com.bazar.bazar_order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazar.bazar_order.model.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer>{

}

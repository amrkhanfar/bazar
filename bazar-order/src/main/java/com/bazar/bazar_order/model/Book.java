package com.bazar.bazar_order.model;

import lombok.Getter;
import lombok.Setter; 

@Getter
@Setter
public class Book {
	private int id;
	private String name;
	private String topic;
	private int quantity;
	private double price;
}

package com.bazar.bazar_frontend.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookInfoDto {
	private String title;
	private int quantity;
	private double price;
}

package com.bazar.bazar_frontend.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookSearchDto {
	private Integer id;
	private String title;
}

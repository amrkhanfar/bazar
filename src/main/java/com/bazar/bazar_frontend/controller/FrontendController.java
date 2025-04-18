package com.bazar.bazar_frontend.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bazar.bazar_frontend.Dto.BookSearchDto;
import com.bazar.bazar_frontend.model.Book;

@RestController
public class FrontendController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/search/{topic}")
	ResponseEntity<List<BookSearchDto>> search(@PathVariable String topic) {
		
		List<Book> queriedBooks;
		try {
			ResponseEntity<Book[]> response = restTemplate
					.getForEntity("http://localhost:8081/catalog/query/topic/" + topic, Book[].class);
			queriedBooks = Arrays.asList(response.getBody());
		} catch (RestClientException ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.emptyList());
		}
		
		List<BookSearchDto> booksForResponse = queriedBooks.stream()
			    .map(book -> BookSearchDto.builder()
			            .id(book.getId())
			            .title(book.getName())
			            .build())
			    .collect(Collectors.toList());
		
		return ResponseEntity.ok(booksForResponse);
	}
}

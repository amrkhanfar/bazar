package com.bazar.bazar_frontend.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bazar.bazar_frontend.Dto.BookInfoDto;
import com.bazar.bazar_frontend.Dto.BookSearchDto;
import com.bazar.bazar_frontend.model.Book;

@RestController
public class FrontendController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/search/{topic}")
	ResponseEntity<?> search(@PathVariable String topic) {
		
		List<Book> queriedBooks;
		try {
			ResponseEntity<Book[]> response = restTemplate
					.getForEntity("http://localhost:8081/catalog/query/topic/" + topic, Book[].class);
			queriedBooks = Arrays.asList(response.getBody());
		} catch (RestClientException ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Catalog service is not available");
		}
		
		List<BookSearchDto> booksForResponse = queriedBooks.stream()
			    .map(book -> BookSearchDto.builder() //line 42
			            .id(book.getId())
			            .title(book.getName())
			            .build())
			    .collect(Collectors.toList());
		
		return ResponseEntity.ok(booksForResponse);
	}
	
	@GetMapping("/info/{id}")
	public ResponseEntity<?> info(@PathVariable int id) {
		
		Book queriedBook;
		try {
			queriedBook = restTemplate
			.getForObject("http://localhost:8081/catalog/query/id/" + id, Book.class);
		} catch (RestClientException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No item found with id: " + id);
		}
		
		BookInfoDto bookForResponse = BookInfoDto.builder()
				.title(queriedBook.getName())
				.quantity(queriedBook.getQuantity())
				.price(queriedBook.getPrice())
				.build();
		
		return ResponseEntity.ok(bookForResponse);
	}
	
	@PostMapping("/purchase/{id}")
	public ResponseEntity<?> purchase(@PathVariable int id) {
		
		String responseMessage;
		try {
			responseMessage = restTemplate.postForObject("http://localhost:8082/order/purchase/id/" + id,
					null, String.class);
		} catch (RestClientException ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Order serivce is not available");
		}
		
		return ResponseEntity.ok(responseMessage);
	}
}

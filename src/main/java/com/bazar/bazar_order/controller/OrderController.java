package com.bazar.bazar_order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bazar.bazar_order.model.Book;
import com.bazar.bazar_order.model.BookUpdateRequest;
import com.bazar.bazar_order.model.Order;
import com.bazar.bazar_order.repository.OrderRepo;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping("/purchase/id/{id}")
	public ResponseEntity<String> purchase(@PathVariable Integer id) {
		Book book;
		
		try {
			book = restTemplate.getForObject("http://localhost:8081/catalog/query/item/" + id,
						Book.class);
		} catch (HttpClientErrorException.NotFound ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Book with id: " + id + " is not found");
		} catch(RestClientException ex) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body("Catalog service is not available");
		}
		
		if (book.getQuantity() <= 0) {
			return ResponseEntity.badRequest().body("The item is out of stock");
		}
		
		BookUpdateRequest bookUpdateRequest = BookUpdateRequest.builder()
				.id(id)
				.quantity(book.getQuantity() - 1)
				.build();
		try {
			restTemplate.put("http://localhost:8081/catalog/query/update",
					bookUpdateRequest);
		} catch (RestClientException ex) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body("Failed to update the stock");
		}
		
		Order order = Order.builder()
				.bookId(book.getId())
				.build();
		
		orderRepo.save(order);
		return ResponseEntity.status(HttpStatus.OK)
				.body("Purchased: " + book.getName());
		
	}
}

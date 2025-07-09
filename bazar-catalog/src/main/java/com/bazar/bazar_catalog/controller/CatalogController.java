package com.bazar.bazar_catalog.controller;

import java.lang.System.Logger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.bazar.bazar_catalog.exception.ResourceNotFoundException;
import com.bazar.bazar_catalog.model.Book;
import com.bazar.bazar_catalog.model.BookUpdateRequest;
import com.bazar.bazar_catalog.repository.BookRepo;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
	
	@Autowired
	private BookRepo bookRepo; 
	
	@Autowired
	private RestTemplate rest;
	
	@Value("${FRONTEND_CACHE_URL:http://localhost:8080}")
	private String frontendCacheUrl;
	
	@GetMapping("/query/topic/{topic}")
	public List<Book> queryByTopic(@PathVariable String topic) {
		List<Book> books = bookRepo.findByTopic(topic);
		return books;

	}
	
	@GetMapping("/query/id/{id}")
	public Book queryById(@PathVariable int id) {
		Book book = bookRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
		return book;
	}
	
	
	@PutMapping("/update")
	public Book updateBook(@RequestBody BookUpdateRequest bookUpdateRequest) {
		Book book = bookRepo.findById(bookUpdateRequest.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookUpdateRequest.getId()));
		
		if (bookUpdateRequest.getPrice() != null) {
			book.setPrice(bookUpdateRequest.getPrice());
		}
		
		if (bookUpdateRequest.getQuantity() != null) {
			book.setQuantity(bookUpdateRequest.getQuantity());
		}
		
		Book updatedBook = bookRepo.save(book);
		invalidateCache(updatedBook.getId());
		return updatedBook;
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> addBook(@RequestBody Book book) {
		Book savedBook;
		try {
			savedBook = bookRepo.save(book);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save");
		}
		
		invalidateCache(savedBook.getId());
		
		return ResponseEntity.status(HttpStatus.OK).body(savedBook.getName() + " Saved successfuly. ID: " + savedBook.getId() );
	}
	
	private void invalidateCache(int bookId) {
		try {
			rest.delete(frontendCacheUrl + "/cache/invalidate/" + bookId);
		} catch (RestClientException ex) {
			
		}
	}
}

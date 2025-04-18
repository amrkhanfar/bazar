package com.bazar.bazar_catalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bazar.bazar_catalog.exception.ResourceNotFoundException;
import com.bazar.bazar_catalog.model.Book;
import com.bazar.bazar_catalog.model.BookUpdateRequest;
import com.bazar.bazar_catalog.repository.BookRepo;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
	
	@Autowired
	private BookRepo bookRepo;
	
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
		
		return bookRepo.save(book);
	}
}

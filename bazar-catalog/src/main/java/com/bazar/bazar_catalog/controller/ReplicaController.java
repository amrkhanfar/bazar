package com.bazar.bazar_catalog.controller;

import com.bazar.bazar_catalog.model.Book;
import com.bazar.bazar_catalog.model.BookUpdateRequest;
import com.bazar.bazar_catalog.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RestController
@RequestMapping("/replica")
public class ReplicaController {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${replica.urls}")
    private List<String> replicaUrls;

    @Value("${replica.id}")
    private String replicaId;

    @PutMapping("/update")
    public void update(@RequestBody BookUpdateRequest request) {
        Book book = bookRepo.findById(request.getId()).orElse(null);
        if (book != null) {
            if (request.getPrice() != null) {
                book.setPrice(request.getPrice());
            }
            if (request.getQuantity() != null) {
                book.setQuantity(request.getQuantity());
            }
            bookRepo.save(book);
        }
    }

    @PostMapping("/add")
    public void add(@RequestBody Book book) {
        bookRepo.save(book);
    }

    public void propagateUpdate(BookUpdateRequest request) {
        for (String url : replicaUrls) {
            if (!url.contains(replicaId)) { // Do not send to self
                restTemplate.put(url + "/replica/update", request);
            }
        }
    }

    public void propagateAdd(Book book) {
        for (String url : replicaUrls) {
            if (!url.contains(replicaId)) { // Do not send to self
                restTemplate.postForObject(url + "/replica/add", book, Book.class);
            }
        }
    }
}

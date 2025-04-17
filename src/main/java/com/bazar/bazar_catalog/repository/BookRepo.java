package com.bazar.bazar_catalog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazar.bazar_catalog.model.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
	List<Book> findByTopic(String topic);
	Optional<Book> findById(int id);
}

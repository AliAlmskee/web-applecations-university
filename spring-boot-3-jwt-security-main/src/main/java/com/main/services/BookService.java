package com.main.services;

import com.main.entity.Book;
import com.main.repository.BookRepository;
import com.main.dto.BookRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "books", allEntries = true)
    public Book save(BookRequest request) {
        var book = Book.builder()
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .build();
        return repository.save(book);
    }

    @Cacheable("books")
    public List<Book> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Book findById(int id) {
        return repository.findByIdWithLock(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    @CacheEvict(value = "books", allEntries = true)
    @Transactional
    public Book update(int id, BookRequest request) {
        Book existing = findById(id);

        existing.setAuthor(request.getAuthor());
        existing.setIsbn(request.getIsbn());

        return repository.save(existing);
    }

    @CacheEvict(value = "books", allEntries = true)
    public void delete(int id) {
        Book existing = findById(id);
        repository.delete(existing);
    }
}

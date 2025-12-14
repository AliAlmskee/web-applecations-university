package com.main.seeder;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookSeeder {
    private final BookRepository bookRepository;

    public BookSeeder(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void seed() {
        if (bookRepository.count() == 0) {
            List<Book> books = new ArrayList<>();

            Book book1 = Book.builder()
                    .author("Harper Lee")
                    .isbn("9780061120084")
                    .build();
            books.add(book1);

            Book book2 = Book.builder()
                    .author("F. Scott Fitzgerald")
                    .isbn("9780743273565")
                    .build();
            books.add(book2);

            Book book3 = Book.builder()
                    .author("George Orwell")
                    .isbn("9780451524935")
                    .build();
            books.add(book3);

            bookRepository.saveAll(books);
        }
    }
}
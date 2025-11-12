package library.seeder;

import library.book.data.Book;
import library.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookSeeder {
    private final BookRepository bookRepository;

    public void seed() {
        if (bookRepository.count() == 0) {
            List<Book> books = new ArrayList<>();

            Book book1 = new Book();
            book1.setTitle("To Kill a Mockingbird");
            book1.setAuthor("Harper Lee");
            book1.setIsbn("9780061120084");
            book1.setPublication_year(1960);
            book1.setNumber_of_copies(3);
            book1.setPrice(15.99);
            book1.setDescription("Pulitzer Prize-winning novel about a young girl's experience with racial injustice in a small Alabama town.");
            books.add(book1);

            Book book2 = new Book();
            book2.setTitle("The Great Gatsby");
            book2.setAuthor("F. Scott Fitzgerald");
            book2.setIsbn("9780743273565");
            book2.setPublication_year(1925);
            book2.setNumber_of_copies(3);
            book2.setPrice(12.99);
            book2.setDescription("Classic novel set in the 1920s about the American Dream and the excesses of wealth.");
            books.add(book2);

            Book book3 = new Book();
            book3.setTitle("1984");
            book3.setAuthor("George Orwell");
            book3.setIsbn("9780451524935");
            book3.setPublication_year(1949);
            book3.setNumber_of_copies(3);
            book3.setPrice(10.99);
            book3.setDescription("Dystopian novel about a totalitarian future society where independent thought is discouraged.");
            books.add(book3);
            books.add(book3);

            bookRepository.saveAll(books);
        }
    }
}
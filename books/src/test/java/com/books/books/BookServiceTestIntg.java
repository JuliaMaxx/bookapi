package com.books.books;

import com.books.books.repository.BookRepository;
import com.books.books.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BookServiceTestIntg {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        // Clear the repository before each test
        bookRepository.deleteAll();
        bookRepository.save(new Book("IT", "Stephen King", 1986, 5));
        bookRepository.save(new Book("Forest Gump", "Winston Groom", 1986, 4));
        bookRepository.save(new Book("1984", "George Orwell", 1949, 3));
        bookRepository.save(new Book("The Shining", "Stephen King", 1977, 3));
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = bookService.getAllBooks();
        assertThat(books).hasSize(4);
    }

    @Test
    public void testFilterBooksByTitle() {
        List<Book> books = bookService.filterBooks("IT", null, null, null);
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("IT");
    }

    @Test
    public void testFilterBooksByAuthor() {
        List<Book> books = bookService.filterBooks(null, "Stephen King", null, null);
        assertThat(books).hasSize(2);
    }

    @Test
    public void testFilterBooksByYear() {
        List<Book> books = bookService.filterBooks(null, null, 1986, null);
        assertThat(books).hasSize(2);
    }

    @Test
    public void testRateBook() {
        Book book = bookRepository.findAll().get(0); // Get the first book
        Book ratedBook = bookService.rateBook(book.getId(), 4);
        assertThat(ratedBook.getRating()).isEqualTo(4);
    }

    @Test
    public void testRateBookWithInvalidRating() {
        Book book = bookRepository.findAll().get(0);
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bookService.rateBook(book.getId(), 6); // Invalid rating
        });
        assertThat(exception.getMessage()).isEqualTo("Rating must be between 1 and 5.");
    }
}

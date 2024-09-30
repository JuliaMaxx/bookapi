package com.books.books;

import com.books.books.repository.BookRepository;
import com.books.books.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;
    List<Book> mockBooks;

    @BeforeEach
    void setUp() {
        // Pre-create mock data without worrying about IDs
        mockBooks = Arrays.asList(
                new Book("IT", "Stephen King", 1986, 5),
                new Book("Forest Gump", "Winston Groom", 1986, 4),
                new Book("1984", "George Orwell", 1949, 3),
                new Book("The Shining", "Stephen King", 1977, 3)
        );

        // Mocking repository methods
        Mockito.when(bookRepository.findAll()).thenReturn(mockBooks);
        Mockito.when(bookRepository.findByTitleContaining("IT")).thenReturn(Arrays.asList(mockBooks.get(0)));
        Mockito.when(bookRepository.findByAuthor("Stephen King")).thenReturn(Arrays.asList(mockBooks.get(0), mockBooks.get(3)));
        Mockito.when(bookRepository.findByReleaseYear(1986)).thenReturn(Arrays.asList(mockBooks.get(0), mockBooks.get(1)));
        Mockito.when(bookRepository.findByRating(4)).thenReturn(Arrays.asList(mockBooks.get(1)));
    }

    @Test
    public void testGetAllBooks() {
        List<Book> result = bookService.getAllBooks();
        Assertions.assertEquals(4, result.size());
    }

    @Test
    public void testFilterBooksByTitle() {
        List<Book> books = bookService.filterBooks("IT", null, null, null);
        Assertions.assertEquals(1, books.size());
        Assertions.assertEquals("IT", books.get(0).getTitle());
    }

    @Test
    public void testFilterBooksByAuthor() {
        List<Book> books = bookService.filterBooks(null, "Stephen King", null, null);
        Assertions.assertEquals(2, books.size());
        Assertions.assertTrue(books.stream().anyMatch(book -> book.getTitle().equals("IT")));
        Assertions.assertTrue(books.stream().anyMatch(book -> book.getTitle().equals("The Shining")));
    }

    @Test
    public void testFilterBooksByYear() {
        List<Book> books = bookService.filterBooks(null, null, 1986, null);
        Assertions.assertEquals(2, books.size());
        Assertions.assertTrue(books.stream().anyMatch(book -> book.getTitle().equals("IT")));
        Assertions.assertTrue(books.stream().anyMatch(book -> book.getTitle().equals("Forest Gump")));
    }

    @Test
    public void testFilterBooksByRating() {
        List<Book> books = bookService.filterBooks(null, null, null, 4);
        Assertions.assertEquals(1, books.size());
        Assertions.assertEquals("Forest Gump", books.get(0).getTitle());
    }

    @Test
    public void testRateBook() {
        // Arrange
        Book bookToRate = mockBooks.get(0);
        bookToRate.setId(1L);
        Mockito.when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(bookToRate));

        // Act
        Book updatedBook = bookService.rateBook(1L, 4);

        // Assert
        Assertions.assertEquals(4, updatedBook.getRating(), "The book's rating should be updated to 4.");
        Mockito.verify(bookRepository).save(bookToRate);
    }

    @Test
    public void testRateBookWrong() {
        // Arrange
        Book bookToRate = mockBooks.get(1);
        bookToRate.setId(2L);
        Mockito.when(bookRepository.findById(2L)).thenReturn(java.util.Optional.of(bookToRate));

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> bookService.rateBook(2L, 6));
    }
}

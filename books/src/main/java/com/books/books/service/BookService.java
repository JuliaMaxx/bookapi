package com.books.books.service;

import com.books.books.Book;
import com.books.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> filterBooks(String title, String author, Integer yeaer, Integer rating) {
        if (title != null) {
            return bookRepository.findByTitleContaining(title);
        } else if (author != null) {
            return bookRepository.findByAuthor(author);
        } else if (yeaer != null) {
            return bookRepository.findByYear(yeaer);
        } else if (rating != null) {
            return bookRepository.findByRating(rating);
        }
        return getAllBooks();
    }

    public Book rateBook(Long id, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        Book book = bookRepository.findById(id).orElseThrow();
        book.setRating(rating);
        bookRepository.save(book);
        return book;
    }
}

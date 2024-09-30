package com.books.books.controllers;
import com.books.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.books.books.service.BookService;
import com.books.books.Book;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author,
                                  @RequestParam(required = false) Integer year,
                                  @RequestParam(required = false) Integer rating) {
        return bookService.filterBooks(title, author, year, rating);
    }

    @PostMapping("/{id}/rate")
    public Book rateBook(@PathVariable Long id, @RequestParam int rating) {
        return bookService.rateBook(id, rating);
    }
}

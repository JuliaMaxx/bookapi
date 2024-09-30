package com.books.books;

import com.books.books.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(BookRepository bookRepository) {
		return args -> {
			bookRepository.save(new Book("IT", "Stephen King", 1986, 5));
			bookRepository.save(new Book("Forest Gump", "Winston Groom", 1986, 4));
			bookRepository.save(new Book("1984", "George Orwell", 1949, 3));
			bookRepository.save(new Book("The Shining", "Stephen King", 1977, 3));
		};
	}

}

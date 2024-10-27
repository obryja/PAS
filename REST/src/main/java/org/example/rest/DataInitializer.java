package org.example.rest;

import org.example.rest.models.Book;
import org.example.rest.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public DataInitializer(BookRepository bookRepository, MongoTemplate mongoTemplate) {
        this.bookRepository = bookRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Czyszczenie kolekcji przed inicjalizacją danych
        mongoTemplate.dropCollection(Book.class);

        // Wczytaj zestaw danych inicjujących
        Book book1 = new Book("To Kill a Mockingbird");
        Book book2 = new Book("The Catcher in the Rye");

        bookRepository.saveAll(Arrays.asList(book1, book2));

        System.out.println("Zainicjalizowano książki.");
    }
}

package org.example.rest;

import org.example.rest.enums.Role;
import org.example.rest.models.Book;
import org.example.rest.models.Rent;
import org.example.rest.models.User;
import org.example.rest.repositories.BookRepository;
import org.example.rest.repositories.RentRepository;
import org.example.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RentRepository rentRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public DataInitializer(BookRepository bookRepository, UserRepository userRepository, RentRepository rentRepository, MongoTemplate mongoTemplate) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.rentRepository = rentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Czyszczenie kolekcji przed inicjalizacją danych
        mongoTemplate.dropCollection(Book.class);
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Rent.class);

        // Wczytaj zestaw danych inicjujących
        Book book1 = new Book("To Kill a Mockingbird");
        Book book2 = new Book("The Catcher in the Rye");

        bookRepository.saveAll(Arrays.asList(book1, book2));

        System.out.println("Zainicjalizowano książki.");

        // Wczytaj zestaw danych inicjujących
        User user1 = new User("client", "client", Role.ROLE_CLIENT);
        User user2 = new User("user", "user", Role.ROLE_USER_ADMIN);
        User user3 = new User("book", "book", Role.ROLE_BOOK_ADMIN);

        userRepository.saveAll(Arrays.asList(user1, user2, user3));

        System.out.println("Zainicjalizowano użytkowników.");

        Rent rent1 = new Rent(user1.getId(), book1.getId(), new Date());

        rentRepository.saveAll(Arrays.asList(rent1));
    }
}

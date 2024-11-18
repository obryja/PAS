package org.example.rest;

import com.mongodb.client.MongoDatabase;
import org.example.rest.models.*;
import org.example.rest.repositories.BookRepository;
import org.example.rest.repositories.RentRepository;
import org.example.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RentRepository rentRepository;
    private final MongoDatabase mongoDatabase;

    @Autowired
    public DataInitializer(BookRepository bookRepository, UserRepository userRepository, RentRepository rentRepository, MongoDatabase mongoDatabase) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.rentRepository = rentRepository;
        this.mongoDatabase = mongoDatabase;
    }

    @Override
    public void run(String... args) throws Exception {
        mongoDatabase.getCollection("books").drop();
        mongoDatabase.getCollection("users").drop();
        mongoDatabase.getCollection("rents").drop();

        // Wczytaj zestaw danych inicjujących
        Book book1 = new Book("To Kill a Mockingbird");
        Book book2 = new Book("The Catcher in the Rye");

        book1 = bookRepository.create(book1);
        book2 = bookRepository.create(book2);

        System.out.println("Zainicjalizowano książki.");

        User user1 = new Admin("admin", "admin", true);
        User user2 = new Manager("manager", "manager", true);
        User user3 = new Client("client", "client", true);

        user1 = userRepository.create(user1);
        user2 = userRepository.create(user2);
        user3 = userRepository.create(user3);

        System.out.println("Zainicjalizowano użytkowników.");

        Rent rent1 = new Rent(user1.getId(), book1.getId(), LocalDateTime.now());

        rent1 = rentRepository.create(rent1);

        System.out.println("Zainicjalizowano wypożyczenia.");
    }
}

package org.example.rest;

import com.mongodb.client.MongoClient;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.example.rest.models.*;
import org.example.rest.repositories.BookRepository;
import org.example.rest.repositories.RentRepository;
import org.example.rest.repositories.UserRepository;
import com.mongodb.client.MongoDatabase;

import java.time.LocalDateTime;

@ApplicationScoped
public class DataInitializer {

    @Inject
    BookRepository bookRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    RentRepository rentRepository;

    @Inject
    MongoDatabase mongoDatabase;

    public void init(@Observes StartupEvent event) {
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

package org.example.rest;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.IndexOptions;
import io.quarkus.runtime.StartupEvent;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.bson.Document;
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
        mongoDatabase.getCollection("users").createIndex(new Document("username", 1), new IndexOptions().unique(true));

        // Wczytaj zestaw danych inicjujących
        Book book1 = new Book("To Kill a Mockingbird");
        Book book2 = new Book("The Catcher in the Rye");

        book1 = bookRepository.create(book1);
        book2 = bookRepository.create(book2);

        System.out.println("Zainicjalizowano książki.");

        User admin1 = new Admin("admin", "admin", true);
        User manager1 = new Manager("manager", "manager", true);
        User client1 = new Client("client", "client", true);
        User client2 = new Client("client2", "client", false);

        admin1 = userRepository.create(admin1);
        manager1 = userRepository.create(manager1);
        client1 = userRepository.create(client1);
        client2 = userRepository.create(client2);

        System.out.println("Zainicjalizowano użytkowników.");

        Rent rent1 = new Rent(client1.getId(), book1.getId(), LocalDateTime.now());

        rent1 = rentRepository.create(rent1);

        System.out.println("Zainicjalizowano wypożyczenia.");
    }
}

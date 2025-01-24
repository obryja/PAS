package org.example.rest;

import com.mongodb.client.MongoDatabase;
import org.example.rest.models.*;
import org.example.rest.repositories.BookRepository;
import org.example.rest.repositories.RentRepository;
import org.example.rest.repositories.UserRepository;
import org.example.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserService userService;
    private final RentRepository rentRepository;
    private final MongoDatabase mongoDatabase;

    @Autowired
    public DataInitializer(BookRepository bookRepository, UserService userService, RentRepository rentRepository, MongoDatabase mongoDatabase) {
        this.bookRepository = bookRepository;
        this.userService = userService;
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
        Book book3 = new Book("1984");
        Book book4 = new Book("Pride and Prejudice");
        Book book5 = new Book("The Great Gatsby");
        Book book6 = new Book("Moby-Dick");
        Book book7 = new Book("War and Peace");
        Book book8 = new Book("The Odyssey");
        Book book9 = new Book("Crime and Punishment");
        Book book10 = new Book("Brave New World");

        book1 = bookRepository.create(book1);
        book2 = bookRepository.create(book2);
        book3 = bookRepository.create(book3);
        book4 = bookRepository.create(book4);
        book5 = bookRepository.create(book5);
        book6 = bookRepository.create(book6);
        book7 = bookRepository.create(book7);
        book8 = bookRepository.create(book8);
        book9 = bookRepository.create(book9);
        book10 = bookRepository.create(book10);

        System.out.println("Zainicjalizowano książki.");

        User admin1 = new Admin("admin", "admin", true);
        User manager1 = new Manager("manager", "manager", true);
        User client1 = new Client("client", "client", true);
        User client2 = new Client("client2", "client", false);
        User client3 = new Client("client3", "client", true);

        admin1 = userService.createUser(admin1);
        manager1 = userService.createUser(manager1);
        client1 = userService.createUser(client1);
        client2 = userService.createUser(client2);
        client3 = userService.createUser(client3);

        System.out.println("Zainicjalizowano użytkowników.");

        Rent rent1 = new Rent(client1.getId(), book1.getId(), LocalDateTime.now().minusDays(7));
        rent1.setEndDate(LocalDateTime.now().minusDays(3));
        Rent rent2 = new Rent(client3.getId(), book2.getId(), LocalDateTime.now().minusDays(5));
        Rent rent3 = new Rent(client1.getId(), book3.getId(), LocalDateTime.now().minusDays(3));

        rent1 = rentRepository.create(rent1);
        rent2 = rentRepository.create(rent2);
        rent3 = rentRepository.create(rent3);

        System.out.println("Zainicjalizowano wypożyczenia.");
    }
}

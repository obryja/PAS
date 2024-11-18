package org.example.rest.services;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import org.example.rest.exceptions.NotFoundException;
import org.example.rest.models.Book;
import org.example.rest.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final MongoClient mongoClient;

    public BookService(BookRepository bookRepository, @Qualifier("mongoClient") MongoClient mongoClient) {
        this.bookRepository = bookRepository;
        this.mongoClient = mongoClient;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(String id) {
        Book book = bookRepository.findById(id);

        if (book == null) {
            throw new NotFoundException("Nie znaleziono książki o podanym ID");
        }

        return book;
    }

    public Book createBook(Book book) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Book createdBook = bookRepository.create(book);

            clientSession.commitTransaction();
            return createdBook;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas tworzenia książki.");
        }
    }

    public Book updateBook(String id, Book changedBook) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Book existingBook =  bookRepository.findById(id);
            if (existingBook == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono książki o podanym ID");
            }

            existingBook.setTitle(changedBook.getTitle());

            Book updatedBook = bookRepository.update(existingBook);

            if (updatedBook == null) {
                clientSession.abortTransaction();
                throw new RuntimeException("Nie udało się zaktualizować książki.");
            }

            clientSession.commitTransaction();
            return updatedBook;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas aktualizacji książki.");
        }
    }

    public void deleteBook(String id) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();
            // TODO - usunięcie tylko jeżeli nie jest z nim związana żadna alokacja
            Book existingBook =  bookRepository.findById(id);
            if (existingBook == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono książki o podanym ID");
            }

            boolean deleteSuccess = bookRepository.delete(id);

            if (!deleteSuccess) {
                clientSession.abortTransaction();
                throw new RuntimeException("Nie udało się usunąć książki.");
            }

            clientSession.commitTransaction();
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas aktualizacji książki.");
        }
    }
}
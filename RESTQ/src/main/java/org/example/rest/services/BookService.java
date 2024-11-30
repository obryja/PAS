package org.example.rest.services;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Qualifier;
import org.bson.types.ObjectId;
import org.example.rest.exceptions.BadRequestException;
import org.example.rest.exceptions.ConflictException;
import org.example.rest.exceptions.NotFoundException;
import org.example.rest.models.Book;
import org.example.rest.models.Rent;
import org.example.rest.repositories.BookRepository;
import org.example.rest.repositories.RentRepository;

import java.util.List;

@ApplicationScoped
public class BookService {
    private final BookRepository bookRepository;
//    private final RentRepository rentRepository;

    @Inject
    MongoClient mongoClient;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(String id) {
        boolean idIsValid = ObjectId.isValid(id);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

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
        boolean idIsValid = ObjectId.isValid(id);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Book existingBook =  bookRepository.findById(id);
            if (existingBook == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono książki o podanym ID");
            }

            existingBook.setTitle(changedBook.getTitle());

            Book updatedBook = bookRepository.update(existingBook);

            clientSession.commitTransaction();
            return updatedBook;
        } catch (NotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas aktualizacji książki.");
        }
    }

    public void deleteBook(String id) {
        boolean idIsValid = ObjectId.isValid(id);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Book existingBook =  bookRepository.findById(id);
            if (existingBook == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono książki o podanym ID");
            }

            /*List<Rent> currentRents = rentRepository.findByBookIdAndEndDateIsNull(existingBook.getId());
            List<Rent> archiveRents = rentRepository.findByBookIdAndEndDateIsNotNull(existingBook.getId());

            if (!currentRents.isEmpty() || !archiveRents.isEmpty()) {
                clientSession.abortTransaction();
                throw new ConflictException("Nie można usunąć książki, ponieważ ma powiązane wypożyczenia.");
            }*/

            bookRepository.delete(id);

            clientSession.commitTransaction();
        } catch (NotFoundException | ConflictException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas usuwania książki.");
        }
    }
}
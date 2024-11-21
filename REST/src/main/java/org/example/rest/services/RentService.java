package org.example.rest.services;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import org.bson.types.ObjectId;
import org.example.rest.exceptions.BadRequestException;
import org.example.rest.exceptions.ConflictException;
import org.example.rest.exceptions.NotFoundException;
import org.example.rest.models.Book;
import org.example.rest.models.Rent;
import org.example.rest.models.User;
import org.example.rest.repositories.BookRepository;
import org.example.rest.repositories.RentRepository;
import org.example.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentService {
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final MongoClient mongoClient;

    public RentService(RentRepository rentRepository, UserRepository userRepository, BookRepository bookRepository, @Qualifier("mongoClient") MongoClient mongoClient) {
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.mongoClient = mongoClient;
    }

    public Rent getRentById(String id) {
        boolean idIsValid = ObjectId.isValid(id);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        Rent rent = rentRepository.findById(id);

        if (rent == null) {
            throw new NotFoundException("Nie znaleziono wypożyczenia o podanym ID");
        }

        return rent;
    }

    public List<Rent> getAllRents() {
        return rentRepository.findAll();
    }

    public List<Rent> getCurrentRentsByUserId(String userId) {
        boolean idIsValid = ObjectId.isValid(userId);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        return rentRepository.findByUserIdAndEndDateIsNull(userId);
    }

    public List<Rent> getArchiveRentsByUserId(String userId) {
        boolean idIsValid = ObjectId.isValid(userId);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        return rentRepository.findByUserIdAndEndDateIsNotNull(userId);
    }

    public List<Rent> getCurrentRentsByBookId(String bookId) {
        boolean idIsValid = ObjectId.isValid(bookId);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        return rentRepository.findByBookIdAndEndDateIsNull(bookId);
    }

    public List<Rent> getArchiveRentsByBookId(String bookId) {
        boolean idIsValid = ObjectId.isValid(bookId);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        return rentRepository.findByBookIdAndEndDateIsNotNull(bookId);
    }

    public Rent createRent(Rent rent) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            User existingUser =  userRepository.findById(rent.getUserId());

            if (existingUser == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono użytkownika o podanym ID");
            }

            Book existingBook =  bookRepository.findById(rent.getBookId());

            if (existingBook == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono książki o podanym ID");
            }

            if (!existingUser.isActive()) {
                clientSession.abortTransaction();
                throw new ConflictException("Użytkownik nie jest aktywny");
            }

            List<Rent> currentRents = rentRepository.findByBookIdAndEndDateIsNull(existingBook.getId());

            if (!currentRents.isEmpty()) {
                clientSession.abortTransaction();
                throw new ConflictException("Książka jest już wypożyczona.");
            }

            Rent createdRent = rentRepository.create(rent);;

            clientSession.commitTransaction();
            return createdRent;
        } catch (NotFoundException | ConflictException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas tworzenia wypożyczenia.");
        }
    }

    public Rent endRent(String id) {
        boolean idIsValid = ObjectId.isValid(id);

        if (!idIsValid) {
            throw new BadRequestException("To nie jest prawidłowy format id, powinien mieć 24 znaki w zapisane w hex");
        }

        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Rent existingRent =  rentRepository.findById(id);

            if (existingRent == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono wypożyczenia o podanym ID");
            }

            if (existingRent.getEndDate() != null) {
                clientSession.abortTransaction();
                throw new ConflictException("Wypożyczenie jest już zakończone");
            }

            LocalDateTime now = LocalDateTime.now();

            if (now.isBefore(existingRent.getBeginDate())) {
                clientSession.abortTransaction();
                throw new ConflictException("Data zakończenia nie może być wcześniejsza niż data rozpoczęcia wypożyczenia");
            }

            existingRent.setEndDate(now);

            Rent updatedRent = rentRepository.update(existingRent);

            clientSession.commitTransaction();
            return updatedRent;
        } catch (NotFoundException | ConflictException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas aktualizacji wypożyczenia.");
        }
    }

    public void deleteRent(String id) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Rent existingRent =  rentRepository.findById(id);
            if (existingRent == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono wypożyczenia o podanym ID");
            }

            if (existingRent.getEndDate() != null) {
                clientSession.abortTransaction();
                throw new ConflictException("Nie można usunąć zakończonego wypożyczenia.");
            }

            rentRepository.delete(id);

            clientSession.commitTransaction();
        } catch (NotFoundException | ConflictException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas usuwania wypożyczenia.");
        }
    }
}
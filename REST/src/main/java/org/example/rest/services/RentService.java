package org.example.rest.services;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import org.example.rest.exceptions.NotFoundException;
import org.example.rest.models.Rent;
import org.example.rest.repositories.RentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentService {
    private final RentRepository rentRepository;
    private final MongoClient mongoClient;

    public RentService(RentRepository rentRepository, @Qualifier("mongoClient") MongoClient mongoClient) {
        this.rentRepository = rentRepository;
        this.mongoClient = mongoClient;
    }

    public Rent getRentById(String id) {
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
        return rentRepository.findByUserIdAndEndDateIsNull(userId);
    }

    public List<Rent> getArchiveRentsByUserId(String userId) {
        return rentRepository.findByUserIdAndEndDateIsNotNull(userId);
    }

    public List<Rent> getCurrentRentsByBookId(String bookId) {
        return rentRepository.findByBookIdAndEndDateIsNull(bookId);
    }

    public List<Rent> getArchiveRentsByBookId(String bookId) {
        return rentRepository.findByBookIdAndEndDateIsNotNull(bookId);
    }

    public Rent createRent(Rent rent) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Rent createdRent = rentRepository.create(rent);;

            clientSession.commitTransaction();
            return createdRent;
        } catch (RuntimeException e) {
            throw new RuntimeException("Wystąpił błąd podczas tworzenia wypożyczenia.");
        }
    }

    public Rent endRent(String id) {
        try (ClientSession clientSession = mongoClient.startSession()) {
            clientSession.startTransaction();

            Rent existingRent =  rentRepository.findById(id);

            if (existingRent == null) {
                clientSession.abortTransaction();
                throw new NotFoundException("Nie znaleziono wypożyczenia o podanym ID");
            }

            existingRent.setEndDate(LocalDateTime.now());

            Rent updatedRent = rentRepository.update(existingRent);

            if (updatedRent == null) {
                clientSession.abortTransaction();
                throw new RuntimeException("Nie udało się zaktualizować wypożyczenia.");
            }

            clientSession.commitTransaction();
            return updatedRent;
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

            boolean deleteSuccess = rentRepository.delete(id);

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
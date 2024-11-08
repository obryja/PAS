package org.example.rest.services;

import org.example.rest.models.Rent;
import org.example.rest.repositories.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RentService {
    private final RentRepository rentRepository;

    public RentService(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    public Rent createRent(Rent rent) {
        return rentRepository.save(rent);
    }

    public Optional<Rent> getRentById(String id) {
        return rentRepository.findById(id);
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

    public Rent endRent(String id) {
        Rent rent = rentRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        rent.setEndDate(new Date());

        return rentRepository.save(rent);
    }

    public void deleteRent(String id) {
        rentRepository.deleteById(id);
    }

}

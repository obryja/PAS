package org.example.rest.repositories;

import org.example.rest.models.Rent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RentRepository extends MongoRepository<Rent, String> {
    List<Rent> findByUserIdAndEndDateIsNotNull(String userId);
    List<Rent> findByUserIdAndEndDateIsNull(String userId);
    List<Rent> findByBookIdAndEndDateIsNotNull(String userId);
    List<Rent> findByBookIdAndEndDateIsNull(String bookId);
}
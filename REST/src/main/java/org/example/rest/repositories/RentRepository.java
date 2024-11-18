package org.example.rest.repositories;

import org.example.rest.models.Rent;

import java.util.List;

public interface RentRepository {
    Rent findById(String id);
    List<Rent> findAll();
    Rent create(Rent rent);
    Rent update(Rent rent);
    boolean delete(String id);
    List<Rent> findByUserIdAndEndDateIsNotNull(String userId);
    List<Rent> findByUserIdAndEndDateIsNull(String userId);
    List<Rent> findByBookIdAndEndDateIsNotNull(String userId);
    List<Rent> findByBookIdAndEndDateIsNull(String bookId);
}

package org.example.rest.repositories;

import org.example.rest.models.Book;

import java.util.List;

public interface BookRepository {
    Book findById(String id);
    List<Book> findAll();
    Book create(Book book);
    Book update(Book book);
    boolean delete(String id);
}

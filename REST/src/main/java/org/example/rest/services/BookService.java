package org.example.rest.services;

import org.example.rest.models.Book;
import org.example.rest.repositories.BookRepository;
import org.example.rest.repositories.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    private final RentRepository rentRepository;

    public BookService(BookRepository bookRepository, RentRepository rentRepository) {
        this.bookRepository = bookRepository;
        this.rentRepository = rentRepository;
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book updateBook(String id, Book changedBook) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));

        existingBook.setTitle(changedBook.getTitle());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(String id) {
        boolean isRented = !rentRepository.findByBookIdAndEndDateIsNull(id).isEmpty();

        bookRepository.deleteById(id);
    }
}

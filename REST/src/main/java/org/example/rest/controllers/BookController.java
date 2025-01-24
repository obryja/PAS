package org.example.rest.controllers;

import jakarta.validation.Valid;
import org.example.rest.models.Book;
import org.example.rest.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody @Valid Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(book));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody @Valid Book book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }
}

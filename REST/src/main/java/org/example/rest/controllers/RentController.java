package org.example.rest.controllers;

import jakarta.validation.Valid;
import org.example.rest.models.Rent;
import org.example.rest.services.RentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rents")
public class RentController {
    private final RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping
    public ResponseEntity<List<Rent>> getAllRents() {
        return ResponseEntity.ok(rentService.getAllRents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rent> getRentById(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getRentById(id));
    }

    @PostMapping
    public ResponseEntity<Rent> createRent(@RequestBody @Valid Rent rent) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentService.createRent(rent));
    }

    @GetMapping("/user/current/{id}")
    public ResponseEntity<List<Rent>> getCurrentRentsByUser(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getCurrentRentsByUserId(id));
    }

    @GetMapping("/user/archive/{id}")
    public ResponseEntity<List<Rent>> getArchiveRentsByUser(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getArchiveRentsByUserId(id));
    }

    @GetMapping("/book/current/{id}")
    public ResponseEntity<List<Rent>> getCurrentRentsByBook(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getCurrentRentsByBookId(id));
    }

    @GetMapping("/book/archive/{id}")
    public ResponseEntity<List<Rent>> getArchiveRentsByBook(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getArchiveRentsByBookId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rent> endRent(@PathVariable String id) {
        return ResponseEntity.ok(rentService.endRent(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRent(@PathVariable String id) {
        rentService.deleteRent(id);
        return ResponseEntity.ok().build();
    }
}

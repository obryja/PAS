package org.example.rest.controllers;

import jakarta.validation.Valid;
import org.example.rest.dto.RentDetailsDTO;
import org.example.rest.models.Rent;
import org.example.rest.models.User;
import org.example.rest.services.RentService;
import org.example.rest.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rents")
public class RentController {
    private final RentService rentService;
    private final UserService userService;

    public RentController(RentService rentService, UserService userService) {
        this.rentService = rentService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public ResponseEntity<List<Rent>> getAllRents() {
        return ResponseEntity.ok(rentService.getAllRents());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<Rent> getRentById(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getRentById(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @PostMapping
    public ResponseEntity<Rent> createRent(@RequestBody @Valid Rent rent) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentService.createRent(rent));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/user/current/{id}")
    public ResponseEntity<List<Rent>> getCurrentRentsByUser(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getCurrentRentsByUserId(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/user/archive/{id}")
    public ResponseEntity<List<Rent>> getArchiveRentsByUser(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getArchiveRentsByUserId(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/book/current/{id}")
    public ResponseEntity<List<Rent>> getCurrentRentsByBook(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getCurrentRentsByBookId(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/book/archive/{id}")
    public ResponseEntity<List<Rent>> getArchiveRentsByBook(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getArchiveRentsByBookId(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PostMapping("/{id}")
    public ResponseEntity<Rent> endRent(@PathVariable String id) {
        return ResponseEntity.ok(rentService.endRent(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRent(@PathVariable String id) {
        rentService.deleteRent(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/details")
    public ResponseEntity<List<RentDetailsDTO>> getAllRentDetails(@RequestParam(value = "current", required = false) Boolean current) {
        return ResponseEntity.ok(rentService.getRentDetails(current));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/user/current/{id}/details")
    public ResponseEntity<List<RentDetailsDTO>> getCurrentRentsDetailsByUser(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getCurrentRentsDetailsByUserId(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/user/archive/{id}/details")
    public ResponseEntity<List<RentDetailsDTO>> getArchiveRentsDetailsByUser(@PathVariable String id) {
        return ResponseEntity.ok(rentService.getArchiveRentsDetailsByUserId(id));
    }

    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping("/user/current/me/details")
    public ResponseEntity<List<RentDetailsDTO>> getCurrentRentsDetailsByMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user1 = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(rentService.getCurrentRentsDetailsByUserId(user1.getId()));
    }

    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping("/user/archive/me/details")
    public ResponseEntity<List<RentDetailsDTO>> getArchiveRentsDetailsByMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user1 = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(rentService.getArchiveRentsDetailsByUserId(user1.getId()));
    }
}

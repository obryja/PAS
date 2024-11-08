package org.example.rest.controllers;

import org.example.rest.models.Rent;
import org.example.rest.services.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rents")
public class RentController {
    private final RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping
    public List<Rent> getAllRents() {
        return rentService.getAllRents();
    }

    @PostMapping
    public Rent createRent(@RequestBody Rent rent) {
        return rentService.createRent(rent);
    }

    @GetMapping("/{id}")
    public Optional<Rent> getRentById(@PathVariable String id) {
        return rentService.getRentById(id);
    }

    @GetMapping("/user/current/{id}")
    public List<Rent> getCurrentRentsByUser(@PathVariable String id) {
        return rentService.getCurrentRentsByUserId(id);
    }

    @GetMapping("/user/archive/{id}")
    public List<Rent> getArchiveRentsByUser(@PathVariable String id) {
        return rentService.getArchiveRentsByUserId(id);
    }

    @GetMapping("/book/current/{id}")
    public List<Rent> getCurrentRentsByBook(@PathVariable String id) {
        return rentService.getCurrentRentsByBookId(id);
    }

    @GetMapping("/book/archive/{id}")
    public List<Rent> getArchiveRentsByBook(@PathVariable String id) {
        return rentService.getArchiveRentsByBookId(id);
    }

    @PutMapping("/{id}")
    public Rent endRent(@PathVariable String id) {
        return rentService.endRent(id);
    }

    @DeleteMapping("/{id}")
    public void deleteRent(@PathVariable String id) {
        rentService.deleteRent(id);
    }
}

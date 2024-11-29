package org.example.mvc.controllers;

import org.example.mvc.services.RentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RentController {
    private final RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping("/rents/list")
    public String listRents(Model model) {
        return "rents/list";
    }

    @GetMapping("/rents/create")
    public String showCreateForm(Model model) {
        return "rents/create";
    }

    /*@GetMapping
    public String listRents(Model model) {
        model.addAttribute("rents", rentService.getAllRents());
        return "rents/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("rent", new Rent());
        return "rents/create";
    }

    @PostMapping("/create")
    public String createRent(@ModelAttribute Rent rent, Model model) {
        try {
            rentService.createRent(rent);
            return "redirect:/rents";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "rents/create";
        }
    }

    @PostMapping("/{id}/end")
    public String endRent(@PathVariable String id) {
        rentService.endRent(id);
        return "redirect:/rents";
    }*/
}

package org.example.mvc.controllers;

import jakarta.validation.Valid;
import org.example.mvc.exceptions.ConflictException;
import org.example.mvc.exceptions.NotFoundException;
import org.example.mvc.models.BookDTO;
import org.example.mvc.models.RentCreateDTO;
import org.example.mvc.models.RentDetailsDTO;
import org.example.mvc.models.UserGetDTO;
import org.example.mvc.services.BookService;
import org.example.mvc.services.RentService;
import org.example.mvc.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rents")
public class RentController {
    private final RentService rentService;
    private final UserService userService;
    private final BookService bookService;

    public RentController(RentService rentService, UserService userService, BookService bookService) {
        this.rentService = rentService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/create")
    public String getRentForm(Model model) {
        List<UserGetDTO> users = userService.getActiveUsers();
        List<BookDTO> books = bookService.getAvailableBooks();

        model.addAttribute("users", users);
        model.addAttribute("books", books);
        model.addAttribute("rent", new RentCreateDTO());

        return "rents/create";
    }

    @GetMapping("/success")
    public String showRentSuccess() {
        return "rents/success";
    }

    @PostMapping
    public String submitRentForm(@ModelAttribute("rent") @Valid RentCreateDTO rent, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<UserGetDTO> users = userService.getActiveUsers();
            List<BookDTO> books = bookService.getAvailableBooks();
            model.addAttribute("users", users);
            model.addAttribute("books", books);

            return "rents/create";
        }

        try {
            rentService.createRent(rent);
            return "redirect:/rents/success";
        } catch (Exception e) {
            List<UserGetDTO> users = userService.getActiveUsers();
            List<BookDTO> books = bookService.getAvailableBooks();
            model.addAttribute("users", users);
            model.addAttribute("books", books);
            model.addAttribute("error", e.getMessage());
            return "rents/create";
        }
    }

    @GetMapping("/list")
    public String getRentList(Model model) {
        List<RentDetailsDTO> rents = rentService.getRents();

        model.addAttribute("rents", rents);

        return "rents/list";
    }

    @PostMapping("/end/{id}")
    public String endRent(@PathVariable String id) {
        try {
            rentService.endRent(id);
            return "redirect:/rents/list";
        } catch (Exception e) {
            return "redirect:/rents/list";
        }


    }

    @PostMapping("/delete/{id}")
    public String deleteRent(@PathVariable String id) {
        try {
            rentService.deleteRent(id);
            return "redirect:/rents/list";
        } catch (Exception e) {
            return "redirect:/rents/list";
        }

    }
}

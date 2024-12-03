package org.example.mvc.controllers;

import jakarta.validation.Valid;
import org.example.mvc.exceptions.ConflictException;
import org.example.mvc.models.UserCuDTO;
import org.example.mvc.models.UserDTO;
import org.example.mvc.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserCuDTO());
        return "users/register";
    }

    @GetMapping("/register/success")
    public String showUserSuccess() {
        return "users/success";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserCuDTO userCuDTO,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "users/register";
        }
        try {
            userService.createUser(userCuDTO);
            return "redirect:/users/register/success";
        } catch (ConflictException e) {
            model.addAttribute("errorUnique", e.getMessage());
            return "users/register";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "users/register";
        }
    }
}
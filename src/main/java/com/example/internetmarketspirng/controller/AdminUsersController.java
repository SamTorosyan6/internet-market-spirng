package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.User;
import com.example.internetmarketspirng.model.UserType;
import com.example.internetmarketspirng.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUsersController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String list(ModelMap modelMap) {
        modelMap.addAttribute("users", userRepository.findAll());
        modelMap.addAttribute("newUser", new User());
        return "admin/users";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/admin/users";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}

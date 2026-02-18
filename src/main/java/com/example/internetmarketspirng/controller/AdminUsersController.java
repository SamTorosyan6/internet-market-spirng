package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.User;
import com.example.internetmarketspirng.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUsersController {

    private static final Logger log = LoggerFactory.getLogger(AdminUsersController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String list(ModelMap modelMap) {
        log.info("Admin users list requested");
        modelMap.addAttribute("users", userRepository.findAll());
        modelMap.addAttribute("newUser", new User());
        return "admin/users";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user) {
        String email = (user != null) ? user.getEmail() : null;
        log.info("Admin add user requested: email={}", email);

        if (user == null || user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            log.warn("Add user rejected: empty password, email={}", email);
            return "redirect:/admin/users?msg=badPassword";
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

            log.info("User created by admin: email={}", email);
            return "redirect:/admin/users";
        } catch (Exception e) {
            log.error("Failed to create user: email={}", email, e);
            return "redirect:/admin/users?msg=addError";
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        log.info("Admin delete user requested: id={}", id);

        try {
            userRepository.deleteById(id);
            log.info("User deleted by admin: id={}", id);
            return "redirect:/admin/users";
        } catch (Exception e) {
            log.error("Failed to delete user: id={}", id, e);
            return "redirect:/admin/users?msg=deleteError";
        }
    }
}

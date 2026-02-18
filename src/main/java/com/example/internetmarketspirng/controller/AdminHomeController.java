package com.example.internetmarketspirng.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    private static final Logger log = LoggerFactory.getLogger(AdminHomeController.class);

    @GetMapping("/home")
    public String home() {
        log.info("Admin home page opened");
        return "admin/home";
    }
}

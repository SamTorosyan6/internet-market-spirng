package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.User;
import com.example.internetmarketspirng.service.UserService;
import com.example.internetmarketspirng.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Value("${internet.market.upload.image.directory.path}")
    private String imageDirectoryPath;

    private final UserService userService;

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        if (springUser != null) {
            log.info("Main page opened by user={}", springUser.getUsername());
            modelMap.addAttribute("user", springUser.getUser());
        } else {
            log.info("Main page opened by anonymous user");
        }
        return "redirect:/categories";
    }

    @GetMapping("/loginPage")
    public String loginPage(@RequestParam(required = false) String msg, ModelMap modelMap) {
        log.info("Login page opened. msg={}", msg);
        modelMap.addAttribute("msg", msg);
        return "loginPage";
    }

    @GetMapping("/registerPage")
    public String registerPage(@RequestParam(required = false) String msg, ModelMap modelMap) {
        log.info("Register page opened. msg={}", msg);
        modelMap.addAttribute("msg", msg);
        return "registerPage";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        String email = (user != null) ? user.getEmail() : null;
        log.info("Register attempt: email={}", email);

        if (email == null || email.trim().isEmpty()) {
            log.warn("Register rejected: empty email");
            return "redirect:/registerPage?msg=Email is required!";
        }

        if (userService.findByEmail(email).isPresent()) {
            log.warn("Register rejected: email already exists: {}", email);
            return "redirect:/registerPage?msg=Username already exists!";
        }

        try {
            userService.save(user);
            log.info("Registration successful: email={}", email);
            return "redirect:/loginPage?msg=Registration successful, please login!";
        } catch (Exception e) {
            log.error("Registration failed: email={}", email, e);
            return "redirect:/registerPage?msg=Registration failed!";
        }
    }

    @GetMapping(value = "/image/get", produces = "image/*")
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) {
        log.info("Image get request: picName={}", picName);

        if (picName == null || picName.trim().isEmpty()) {
            log.warn("Image get rejected: empty picName");
            return null;
        }

        File file = new File(imageDirectoryPath, picName);

        if (!file.exists() || !file.isFile()) {
            log.warn("Image not found: path={}", file.getAbsolutePath());
            return null;
        }

        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            log.info("Image loaded successfully: picName={}, size={} bytes", picName, bytes.length);
            return bytes;
        } catch (IOException e) {
            log.error("Failed to read image: path={}", file.getAbsolutePath(), e);
            return null;
        }
    }
}

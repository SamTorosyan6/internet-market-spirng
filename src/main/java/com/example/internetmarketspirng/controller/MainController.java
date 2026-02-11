package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.User;
import com.example.internetmarketspirng.service.UserService;
import com.example.internetmarketspirng.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
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

    @Value("${internet.market.upload.image.directory.path}")
    private String imageDirectoryPath;

    private final UserService userService;

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal SpringUser springUser,
                           ModelMap modelMap) {
        if (springUser != null) {
            modelMap.addAttribute("user", springUser.getUser());
        }
        return "redirect:/categories";
    }

    @GetMapping("/loginPage")
    public String loginPage(@RequestParam(required = false) String msg, ModelMap modelMap) {
        modelMap.addAttribute("msg", msg);
        return "loginPage";
    }

    @GetMapping("/registerPage")
    public String registerPage(@RequestParam(required = false) String msg, ModelMap modelMap) {
        modelMap.addAttribute("msg", msg);
        return "registerPage";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return "redirect:/registerPage?msg=Username already exists!";
        }
        userService.save(user);
        return "redirect:/loginPage?msg=Registration successful, please login!";
    }

    @GetMapping(value = "/image/get", produces = "image/*")
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) {
        File file = new File(imageDirectoryPath, picName);
        if (file.exists() && file.isFile()) {
            try {
                return FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.Product;
import com.example.internetmarketspirng.service.CategoryService;
import com.example.internetmarketspirng.service.ImageStorageService;
import com.example.internetmarketspirng.service.ProductService;
import com.example.internetmarketspirng.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageStorageService imageStorageService;

    @GetMapping
    public String list(ModelMap modelMap) {
        modelMap.addAttribute("products", productService.findAll());
        return "admin/products";
    }

    @GetMapping("/add")
    public String addPage(ModelMap modelMap) {
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("product", new Product());
        return "admin/product-form";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute Product product,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal SpringUser springUser
    ) {
        product.setCategory(categoryService.findById(categoryId));
        String picName = imageStorageService.saveProductImage(image);
        product.setPicName(picName);
        product.setCreatedAt(LocalDateTime.now());
        product.setUser(springUser.getUser());
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        Product p = productService.findById(id);
        if (p != null) {
            imageStorageService.deleteIfExists(p.getPicName());
            productService.deleteById(id);
        }
        return "redirect:/admin/products";
    }
}

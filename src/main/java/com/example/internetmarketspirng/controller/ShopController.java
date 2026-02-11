package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.Product;
import com.example.internetmarketspirng.service.CategoryService;
import com.example.internetmarketspirng.service.ProductService;
import com.example.internetmarketspirng.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping("/categories")
    public String categories(ModelMap modelMap) {
        modelMap.addAttribute("categories", categoryService.findAll());
        return "shop/categories";
    }

    @GetMapping("/categories/{id}/products")
    public String productsByCategory(@PathVariable int id, ModelMap modelMap) {
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("products", productRepository.findByCategoryId(id));
        return "shop/products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable int id, ModelMap modelMap) {
        Product product = productService.findById(id);
        modelMap.addAttribute("product", product);
        return "shop/product-detail";
    }
}

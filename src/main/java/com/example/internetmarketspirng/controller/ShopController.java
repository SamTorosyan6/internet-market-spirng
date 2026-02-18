package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.Product;
import com.example.internetmarketspirng.repository.CommentRepository;
import com.example.internetmarketspirng.repository.ProductRepository;
import com.example.internetmarketspirng.service.CategoryService;
import com.example.internetmarketspirng.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private static final Logger log = LoggerFactory.getLogger(ShopController.class);

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final CommentRepository commentRepository;

    @GetMapping("/categories")
    public String categories(ModelMap modelMap) {
        log.info("Categories page requested");
        modelMap.addAttribute("categories", categoryService.findAll());
        return "shop/categories";
    }

    @GetMapping("/categories/{id}/products")
    public String productsByCategory(@PathVariable int id, ModelMap modelMap) {
        log.info("Products by category requested: categoryId={}", id);

        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("products", productRepository.findByCategoryId(id));

        return "shop/products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable int id, ModelMap modelMap) {
        log.info("Product detail requested: productId={}", id);

        Product product = productService.findById(id);
        if (product == null) {
            log.warn("Product not found: productId={}", id);
            return "redirect:/categories";
        }

        modelMap.addAttribute("product", product);
        modelMap.addAttribute("comments", commentRepository.findAllByProduct_IdOrderByCreatedAtDesc(id));

        if (product.getCategory() != null) {
            modelMap.addAttribute("backUrl", "/categories/" + product.getCategory().getId() + "/products");
        } else {
            log.warn("Product category is null: productId={}", id);
            modelMap.addAttribute("backUrl", "/categories");
        }

        return "shop/product-detail";
    }
}

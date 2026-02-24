package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.Product;
import com.example.internetmarketspirng.repository.CommentRepository;
import com.example.internetmarketspirng.repository.ProductRepository;
import com.example.internetmarketspirng.service.CategoryService;
import com.example.internetmarketspirng.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private static final Logger log = LoggerFactory.getLogger(ShopController.class);

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final CommentRepository commentRepository;

    @GetMapping("/categories")
    public String categories(ModelMap modelMap,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);

        Page<Product> result = productService.findAll(pageRequest);
        int totalPages = result.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        log.info("Categories page requested");
        modelMap.addAttribute("categories", categoryService.findAll());
        return "shop/categories";
    }

    @GetMapping("/categories/{id}/products")
    public String productsByCategory(@PathVariable int id,
                                     ModelMap modelMap,
                                     @RequestParam("page") Optional<Integer> page,
                                     @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);

        Page<Product> result = productService.findByCategoryId(id, pageRequest);
        int totalPages = result.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }

        log.info("Products by category requested: categoryId={}, page={}", id, currentPage);

        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("products", result);
        modelMap.addAttribute("categoryId", id);

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

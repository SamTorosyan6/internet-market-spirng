package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.Product;
import com.example.internetmarketspirng.service.CategoryService;
import com.example.internetmarketspirng.service.ImageStorageService;
import com.example.internetmarketspirng.service.ProductService;
import com.example.internetmarketspirng.service.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {

    private static final Logger log = LoggerFactory.getLogger(AdminProductController.class);

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageStorageService imageStorageService;

    @GetMapping
    public String list(ModelMap modelMap,
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

        log.info("Admin products list requested");
        modelMap.addAttribute("products", result);
        return "admin/products";
    }

    @GetMapping("/add")
    public String addPage(ModelMap modelMap) {
        log.info("Admin product add page requested");
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("product", new Product());
        modelMap.addAttribute("mode", "add");
        return "admin/product-form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Product product,
                      @RequestParam("categoryId") int categoryId,
                      @RequestParam("image") MultipartFile image,
                      @AuthenticationPrincipal SpringUser springUser) {
        String adminEmail = (springUser != null && springUser.getUser() != null) ? springUser.getUser().getEmail() : "unknown";
        String originalFilename = (image != null) ? image.getOriginalFilename() : null;

        log.info("Admin product add requested: title={}, categoryId={}, image={}, admin={}",
                product.getTitle(), categoryId, originalFilename, adminEmail);

        try {
            product.setCategory(categoryService.findById(categoryId));

            String picName = imageStorageService.saveProductImage(image);
            product.setPicName(picName);

            product.setCreatedAt(LocalDateTime.now());
            if (springUser != null) {
                product.setUser(springUser.getUser());
            }

            productService.save(product);

            log.info("Admin product created successfully: title={}, categoryId={}, picName={}, admin={}",
                    product.getTitle(), categoryId, picName, adminEmail);

            return "redirect:/admin/products";
        } catch (Exception e) {
            log.error("Failed to add product: title={}, categoryId={}, admin={}", product.getTitle(), categoryId, adminEmail, e);
            return "redirect:/admin/products?msg=addError";
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        log.info("Admin product delete requested: id={}", id);

        try {
            Product p = productService.findById(id);
            if (p == null) {
                log.warn("Product not found for delete: id={}", id);
                return "redirect:/admin/products?msg=notFound";
            }

            imageStorageService.deleteIfExists(p.getPicName());
            productService.deleteById(id);

            log.info("Admin product deleted: id={}, picName={}", id, p.getPicName());
            return "redirect:/admin/products";
        } catch (Exception e) {
            log.error("Failed to delete product: id={}", id, e);
            return "redirect:/admin/products?msg=deleteError";
        }
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable int id, ModelMap modelMap) {
        log.info("Admin product edit page requested: id={}", id);

        Product product = productService.findById(id);
        if (product == null) {
            log.warn("Product not found for edit page: id={}", id);
            return "redirect:/admin/products?msg=notFound";
        }

        modelMap.addAttribute("product", product);
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("mode", "edit");
        return "admin/product-form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable int id,
                       @ModelAttribute Product formProduct,
                       @RequestParam("categoryId") int categoryId,
                       @RequestParam(value = "image", required = false) MultipartFile image) {

        String newImageName = (image != null) ? image.getOriginalFilename() : null;
        log.info("Admin product edit requested: id={}, newTitle={}, categoryId={}, newImage={}",
                id, formProduct.getTitle(), categoryId, newImageName);

        try {
            Product dbProduct = productService.findById(id);
            if (dbProduct == null) {
                log.warn("Product not found for edit: id={}", id);
                return "redirect:/admin/products?msg=notFound";
            }

            dbProduct.setTitle(formProduct.getTitle());
            dbProduct.setPrice(formProduct.getPrice());
            dbProduct.setDescription(formProduct.getDescription());
            dbProduct.setCategory(categoryService.findById(categoryId));

            if (image != null && !image.isEmpty()) {
                imageStorageService.deleteIfExists(dbProduct.getPicName());
                String picName = imageStorageService.saveProductImage(image);
                dbProduct.setPicName(picName);
                log.info("Product image updated: id={}, newPicName={}", id, picName);
            }

            productService.save(dbProduct);
            log.info("Admin product updated successfully: id={}", id);

            return "redirect:/admin/products";
        } catch (Exception e) {
            log.error("Failed to edit product: id={}", id, e);
            return "redirect:/admin/products?msg=editError";
        }
    }
}

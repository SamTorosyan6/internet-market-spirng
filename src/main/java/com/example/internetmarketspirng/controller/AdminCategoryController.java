package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.Category;
import com.example.internetmarketspirng.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private static final Logger log = LoggerFactory.getLogger(AdminCategoryController.class);

    private final CategoryService categoryService;

    @GetMapping
    public String list(ModelMap modelMap) {
        log.info("Admin categories list requested");
        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("category", new Category());
        return "admin/categories";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Category category) {
        try {
            log.info("Admin category save requested: id={}, name={}", category.getId(), category.getName());
            categoryService.save(category);
            log.info("Admin category saved: id={}, name={}", category.getId(), category.getName());
            return "redirect:/admin/categories";
        } catch (Exception e) {
            log.error("Failed to save category: id={}, name={}", category.getId(), category.getName(), e);
            return "redirect:/admin/categories?msg=saveError";
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        try {
            log.info("Admin category delete requested: id={}", id);
            categoryService.deleteById(id);
            log.info("Admin category deleted: id={}", id);
            return "redirect:/admin/categories";
        } catch (Exception e) {
            log.error("Failed to delete category: id={}", id, e);
            return "redirect:/admin/categories?msg=deleteError";
        }
    }

    @GetMapping("/edit")
    public String edit(@RequestParam int id, ModelMap modelMap) {
        log.info("Admin category edit page requested: id={}", id);

        Category category = categoryService.findById(id);
        if (category == null) {
            log.warn("Category not found for edit: id={}", id);
            return "redirect:/admin/categories?msg=notFound";
        }

        modelMap.addAttribute("categories", categoryService.findAll());
        modelMap.addAttribute("category", category);
        return "admin/categories";
    }
}

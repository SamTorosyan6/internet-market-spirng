package com.example.internetmarketspirng.service.impl;

import com.example.internetmarketspirng.model.Category;
import com.example.internetmarketspirng.repository.CategoryRepository;
import com.example.internetmarketspirng.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
       return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
       return categoryRepository.save(category);
    }

    @Override
    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }
}

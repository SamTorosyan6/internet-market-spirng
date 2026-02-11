package com.example.internetmarketspirng.service;

import com.example.internetmarketspirng.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category save(Category category);

    Category findById(Integer id);

    void deleteById(Integer id);

}

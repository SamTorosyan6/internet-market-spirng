package com.example.internetmarketspirng.service;

import com.example.internetmarketspirng.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product save(Product product);

    Product findById(Integer id);

    void deleteById(Integer id);

}

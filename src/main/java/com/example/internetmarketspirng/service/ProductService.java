package com.example.internetmarketspirng.service;

import com.example.internetmarketspirng.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Product save(Product product);

    Product findById(Integer id);

    void deleteById(Integer id);

    Page<Product> findByCategoryId(int categoryId, Pageable pageable);

}

package com.example.internetmarketspirng.repository;

import com.example.internetmarketspirng.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByCategoryId(int categoryId, Pageable pageable);
}


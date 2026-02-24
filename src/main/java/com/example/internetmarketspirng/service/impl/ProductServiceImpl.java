package com.example.internetmarketspirng.service.impl;

import com.example.internetmarketspirng.model.Product;
import com.example.internetmarketspirng.repository.ProductRepository;
import com.example.internetmarketspirng.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product save(Product product) {
       return productRepository.save(product);
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> findByCategoryId(int categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }
}

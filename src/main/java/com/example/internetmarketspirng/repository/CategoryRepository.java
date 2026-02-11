package com.example.internetmarketspirng.repository;

import com.example.internetmarketspirng.model.Category;
import com.example.internetmarketspirng.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {


}

package com.example.internetmarketspirng.repository;

import com.example.internetmarketspirng.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByProduct_IdOrderByCreatedAtDesc(Integer productId);
}
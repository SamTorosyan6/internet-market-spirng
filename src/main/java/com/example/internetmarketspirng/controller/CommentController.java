package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.Comment;
import com.example.internetmarketspirng.model.Product;
import com.example.internetmarketspirng.model.User;
import com.example.internetmarketspirng.repository.CommentRepository;
import com.example.internetmarketspirng.repository.UserRepository;
import com.example.internetmarketspirng.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor

public class CommentController {

    private final CommentRepository commentRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    @PostMapping("/products/{productId}/comments")
    public String addComment(@PathVariable int productId, @RequestParam("comment") String commentText, Authentication authentication) {
        if (commentText == null || commentText.trim().isEmpty()) {
            return "redirect:/products/" + productId + "?commentError=empty";
        }
        if (commentText.length() > 500) {
            return "redirect:/products/" + productId + "?commentError=tooLong";
        }

        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("User not found: " + username));

        Product product = productService.findById(productId);
        if (product == null) {
            return "redirect:/categories";
        }

        Comment comment = Comment.builder()
                .comment(commentText.trim())
                .product(product)
                .user(user)
                .build();
        commentRepository.save(comment);
        return "redirect:/products/" + productId;
    }

}

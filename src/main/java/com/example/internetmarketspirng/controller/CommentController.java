package com.example.internetmarketspirng.controller;

import com.example.internetmarketspirng.model.Comment;
import com.example.internetmarketspirng.model.Product;
import com.example.internetmarketspirng.model.User;
import com.example.internetmarketspirng.repository.CommentRepository;
import com.example.internetmarketspirng.repository.UserRepository;
import com.example.internetmarketspirng.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @PostMapping("/products/{productId}/comments")
    public String addComment(@PathVariable int productId, @RequestParam("comment") String commentText, Authentication authentication) {

        String username = (authentication != null) ? authentication.getName() : "anonymous";
        int length = (commentText == null) ? 0 : commentText.length();

        logger.info("Add comment request: productId={}, user={}, length={}", productId, username, length);

        if (commentText == null || commentText.trim().isEmpty()) {
            logger.warn("Empty comment rejected: productId={}, user={}", productId, username);
            return "redirect:/products/" + productId + "?commentError=empty";
        }
        if (commentText.length() > 500) {
            logger.warn("Too long comment rejected: productId={}, user={}, length={}", productId, username, commentText.length());
            return "redirect:/products/" + productId + "?commentError=tooLong";
        }

        User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalStateException("User not found: " + username));

        Product product = productService.findById(productId);
        if (product == null) {
            logger.warn("Product not found for comment: productId={}, user={}", productId, username);
            return "redirect:/categories";
        }

        Comment comment = Comment.builder()
                .comment(commentText.trim())
                .product(product)
                .user(user)
                .build();
        commentRepository.save(comment);
        logger.info("Comment saved: productId={}, user={}", productId, username);
        return "redirect:/products/" + productId;
    }

}

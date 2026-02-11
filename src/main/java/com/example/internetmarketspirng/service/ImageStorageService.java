package com.example.internetmarketspirng.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String saveProductImage(MultipartFile file);
    void deleteIfExists(String fileName);
}

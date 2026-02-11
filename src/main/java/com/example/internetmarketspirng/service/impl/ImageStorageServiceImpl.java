package com.example.internetmarketspirng.service.impl;

import com.example.internetmarketspirng.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    @Value("${internet.market.upload.image.directory.path}")
    private String uploadDir;

    @Override
    public String saveProductImage(MultipartFile file) {
        if (uploadDir == null || uploadDir.isBlank()) {
            throw new RuntimeException("Upload directory path is empty. Check application.properties");
        }

        if (file == null || file.isEmpty()) return null;

        try {
            Path dirPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dirPath);

            String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
            String ext = "";

            int dot = original.lastIndexOf(".");
            if (dot >= 0) ext = original.substring(dot);

            String fileName = UUID.randomUUID() + ext;
            Path target = dirPath.resolve(fileName);

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteIfExists(String fileName) {
        if (fileName == null || fileName.isBlank()) return;

        try {
            Path path = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName);
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}

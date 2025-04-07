package com.lucasrech.confeitandoapi.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class ImageUtils {

    public static String saveImage(MultipartFile file, String uploadDir) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        } else if(!isValidImage(file)) {
            throw new IllegalArgumentException("Invalid format image");
        } else {
            Path uploadPath = Paths.get(uploadDir);
            if (!uploadPath.toFile().exists()) {
                java.nio.file.Files.createDirectories(uploadPath);
            }

            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(Objects.requireNonNull(fileName));
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString();
        }
    }

    public static String saveMultipleImages(MultipartFile[] files, String uploadDir) throws IOException {
        if (files.length == 0) {
            throw new IllegalArgumentException("Files are empty");
        }

        StringBuilder filePaths = new StringBuilder();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            } else if(!isValidImage(file)) {
                throw new IllegalArgumentException("Invalid format image");
            } else {
                Path uploadPath = Paths.get(uploadDir);
                if (!uploadPath.toFile().exists()) {
                    java.nio.file.Files.createDirectories(uploadPath);
                }

                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(Objects.requireNonNull(fileName));
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                filePaths.append(filePath).append(",");
            }
        }
        return filePaths.toString();
    }

    private static boolean isValidImage(MultipartFile file) {
        String contentType = file.getContentType();
        assert contentType != null;
        return contentType.equals("image/jpeg") || contentType.equals("image/png");
    }
}

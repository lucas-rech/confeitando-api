package com.lucasrech.confeitandoapi.utils;

import com.lucasrech.confeitandoapi.exceptions.ImageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;


public class ImageUtils {

    /**
     * Saves an image to the specified directory with a validated name and extension.
     *
     * @param file      the MultipartFile object
     * @param uploadDir the complete directory to save the image
     * @param name      the name of the image
     * @return the path of the saved image
     * @throws IOException if an I/O error occurs
     */
    public static String saveImage(MultipartFile file, String uploadDir, String name) throws IOException {
        if (file.isEmpty()) {
            throw new ImageException("File is empty");
        } else if(!isValidImage(file)) {
            throw new ImageException("Invalid format image");
        } else {
            Path uploadPath = Paths.get(uploadDir);
            if (!uploadPath.toFile().exists()) {
                java.nio.file.Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(validateImageNameAndExtension(name, file));
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString().replace("\\", "/");
        }
    }

    public static String saveMultipleImages(MultipartFile[] files, String uploadDir) throws IOException {
        if (files.length == 0) {
            throw new ImageException("Files are empty");
        }

        StringBuilder filePaths = new StringBuilder();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new ImageException("File is empty");
            } else if(!isValidImage(file)) {
                throw new ImageException("Invalid format image");
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

    /**
     * Validates the image name and extension.
     *
     * @param imageName the name of the image
     * @param file      the MultipartFile object
     * @return the validated image name with extension
     */
    public static String validateImageNameAndExtension(String imageName, MultipartFile file) {
        imageName = imageName.replaceAll(" ", "_");
        imageName = imageName.replaceAll("[^a-zA-Z0-9_]", "");
        imageName = imageName.toLowerCase();

        String extension = Objects.equals(file.getContentType(), "image/jpeg") ? ".jpg" : ".png";
        imageName = imageName + extension;

        return imageName;
    }
}

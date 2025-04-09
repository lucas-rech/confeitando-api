package com.lucasrech.confeitandoapi.flavor.dtos;

import org.springframework.web.multipart.MultipartFile;

public record FlavorDTO(
        String title,
        String description,
        Double price,
        MultipartFile image
) {
}

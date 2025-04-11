package com.lucasrech.confeitandoapi.flavor.dtos;

import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record FlavorUpdateRequestDTO(
        Integer id,
        String title,
        String description,
        Double price,
        @Nullable MultipartFile image
) {

}

package com.lucasrech.confeitandoapi.flavor.dtos;

public record FlavorResponseDTO(
        String title,
        String description,
        Double price,
        String imageUrl
) {
}

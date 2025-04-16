package com.lucasrech.confeitandoapi.user.dto;

public record UserRequestDTO(
        String name,
        String email,
        String password
) {
}

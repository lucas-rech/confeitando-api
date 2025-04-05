package com.lucasrech.confeitandoapi.flavor;

import org.springframework.web.multipart.MultipartFile;

public record FlavorDTO(String title, String description, Double value, MultipartFile image) {
}

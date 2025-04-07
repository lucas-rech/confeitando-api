package com.lucasrech.confeitandoapi.flavor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/flavors")
public class FlavorController {
    private final FlavorService flavorService;

    public FlavorController(FlavorService flavorService) {
        this.flavorService = flavorService;
    }

    //TODO: Criar um DTO para o retorno do método
    @PostMapping("/create")
    public ResponseEntity<FlavorDTO> saveFlavor(@ModelAttribute FlavorDTO flavorDTO) throws IOException {
        try {
            flavorService.createFlavor(flavorDTO);
            return ResponseEntity.ok(flavorDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new FlavorDTO(
                            null,
                            "Error: " + e.getMessage(),
                            null,
                            null
                    )
            );
        }
    }
}

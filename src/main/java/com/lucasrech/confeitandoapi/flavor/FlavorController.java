package com.lucasrech.confeitandoapi.flavor;


import com.lucasrech.confeitandoapi.flavor.dtos.FlavorDTO;
import com.lucasrech.confeitandoapi.flavor.dtos.FlavorResponseDTO;
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
    public ResponseEntity<FlavorResponseDTO> saveFlavor(@ModelAttribute FlavorDTO flavorDTO) throws IOException {
        flavorService.createFlavor(flavorDTO);

        FlavorEntity flavor = flavorService.getFlavorByTitle(flavorDTO.title());

        FlavorResponseDTO FlavorResponseDTO = new FlavorResponseDTO(
                flavor.getTitle(),
                flavor.getDescription(),
                flavor.getPrice(),
                flavor.getImageUrl()
        );

        return ResponseEntity.ok(FlavorResponseDTO);
    }
}

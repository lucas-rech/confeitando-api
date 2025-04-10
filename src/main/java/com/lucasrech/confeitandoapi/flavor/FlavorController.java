package com.lucasrech.confeitandoapi.flavor;


import com.lucasrech.confeitandoapi.flavor.dtos.FlavorDTO;
import com.lucasrech.confeitandoapi.flavor.dtos.FlavorDeleteRequestDTO;
import com.lucasrech.confeitandoapi.flavor.dtos.FlavorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/flavors")
public class FlavorController {
    private final FlavorService flavorService;

    public FlavorController(FlavorService flavorService) {
        this.flavorService = flavorService;
    }

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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFlavor(FlavorDeleteRequestDTO flavorDeleteRequestDTO) {
        flavorService.deleteFlavor(flavorDeleteRequestDTO.id());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<FlavorResponseDTO>> getAllFlavors() {
        List<FlavorEntity> flavors = flavorService.getAllFlavors();
        List<FlavorResponseDTO> flavorResponseDTOs = flavors.stream()
                .map(flavor -> new FlavorResponseDTO(
                        flavor.getTitle(),
                        flavor.getDescription(),
                        flavor.getPrice(),
                        flavor.getImageUrl()
                ))
                .toList();

        return ResponseEntity.ok(flavorResponseDTOs);
    }
}

package com.lucasrech.confeitandoapi.flavor;


import com.lucasrech.confeitandoapi.flavor.dtos.FlavorDTO;
import com.lucasrech.confeitandoapi.flavor.dtos.FlavorDeleteRequestDTO;
import com.lucasrech.confeitandoapi.flavor.dtos.FlavorResponseDTO;
import com.lucasrech.confeitandoapi.flavor.dtos.FlavorUpdateRequestDTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/flavors")
@Tag(name = "Flavor", description = "Flavor API")
public class FlavorController {
    private final FlavorService flavorService;

    public FlavorController(FlavorService flavorService) {
        this.flavorService = flavorService;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Create Flavor",
            description = "Create a new flavor with title, description, price and image"
    )
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

    @Operation(
            summary = "Delete Flavor",
            description = "Delete a flavor by ID"
    )
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFlavor(FlavorDeleteRequestDTO flavorDeleteRequestDTO) throws IOException {
        flavorService.deleteFlavor(flavorDeleteRequestDTO.id());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get all Flavors",
            description = "Get all flavors with title, description, price and image"
    )
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

    @Operation(
            summary = "Update Flavor",
            description = "Update a flavor by ID"
    )
    @PutMapping("/update")
    public ResponseEntity<FlavorResponseDTO> updateFlavor(@ModelAttribute FlavorUpdateRequestDTO flavorUpdateRequestDTO) throws IOException {
        flavorService.updateFlavor(flavorUpdateRequestDTO.id(), flavorUpdateRequestDTO);

        FlavorEntity flavor = flavorService.getFlavorById(flavorUpdateRequestDTO.id());

        FlavorResponseDTO FlavorResponseDTO = new FlavorResponseDTO(
                flavor.getTitle(),
                flavor.getDescription(),
                flavor.getPrice(),
                flavor.getImageUrl()
        );

        return ResponseEntity.ok(FlavorResponseDTO);
    }
}

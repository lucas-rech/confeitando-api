package com.lucasrech.confeitandoapi.flavor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FlavorService {

    @Value("${upload.dir}")
    private String uploadDir;

    //TODO: Criar tratamento de exceções para os métodos

    private final FlavorRepository flavorRepository;

    public FlavorService(FlavorRepository flavorRepository) {
        this.flavorRepository = flavorRepository;
    }

    /*
    TODO: Criar um método para verificar se o arquivo é uma imagem válida
    TODO: Separar a lógica de upload de imagem em um serviço separado
     */
    public String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!uploadPath.toFile().exists()) {
            java.nio.file.Files.createDirectories(uploadPath);
        }

        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(Objects.requireNonNull(fileName));
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    public FlavorEntity createFlavor(FlavorDTO flavorDTO) throws IOException {
        if(getFlavorByTitle(flavorDTO.title()) != null) {
            throw new IllegalArgumentException("Flavor with this title already exists");
        }

        //Salva a imagem
        String imagePath = saveImage(flavorDTO.image());


        //Cria o objeto FlavorEntity
        FlavorEntity flavor = new FlavorEntity(
                flavorDTO.title(),
                flavorDTO.description(),
                flavorDTO.value(),
                imagePath
        );
        return flavorRepository.save(flavor);
    }

    public FlavorEntity getFlavorById(Integer id) {
        return flavorRepository.findById(id).orElse(null);
    }

    public FlavorEntity getFlavorByTitle(String title) {
        if(title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        } else if (flavorRepository.findByTitle(title) == null) {
            throw new IllegalArgumentException("Flavor with this title does not exist");
        }
        return flavorRepository.findByTitle(title);
    }

    public void deleteFlavor(Integer id) {
        flavorRepository.deleteById(id);
    }

    public FlavorEntity updateFlavor(Integer id, FlavorEntity updatedFlavor) {
        if (flavorRepository.existsById(id)) {
            updatedFlavor.setId(id);
            return flavorRepository.save(updatedFlavor);
        }
        return null;
    }
}

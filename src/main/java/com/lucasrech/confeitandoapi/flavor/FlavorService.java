package com.lucasrech.confeitandoapi.flavor;

import com.lucasrech.confeitandoapi.exceptions.FlavorException;
import com.lucasrech.confeitandoapi.flavor.dtos.FlavorDTO;
import com.lucasrech.confeitandoapi.flavor.dtos.FlavorUpdateRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


import static com.lucasrech.confeitandoapi.utils.ImageUtils.*;

@Service
public class FlavorService {


    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${root.dir}")
    private String rootDir;

    private final FlavorRepository flavorRepository;

    @Autowired
    public FlavorService(FlavorRepository flavorRepository) {
        this.flavorRepository = flavorRepository;
    }

    public FlavorService(FlavorRepository flavorRepository, String uploadDir) {
        this.flavorRepository = flavorRepository;
        this.uploadDir = uploadDir;
    }

    /*
    TODO: Limitar o tamanho do arquivo
    TODO: Criar um método para deletar a imagem do servidor quando o sabor for deletado
    TODO: Criar um método para atualizar a imagem do sabor
     */

    /**
     * Esse método realiza duas operações importantes:
     * 1. Efetua o upload da imagem do sabor para o servidor.
     * 2. Cria um novo sabor no banco de dados com o caminho da imagem.
     *
     * @param flavorDTO Objeto que contém os dados do sabor a ser criado.
     * @throws IOException Se ocorrer um erro ao salvar a imagem.
     */
    public void createFlavor(FlavorDTO flavorDTO) throws IOException {
        if(getFlavorByTitle(flavorDTO.title()) != null) {
            throw new FlavorException("Flavor with this title already exists");
        }

        //Salva a imagem e cria caminho filtrado para salvar no banco de dados apenas o nome da imagem e o diretório resources nesse caso
        String imageName = validateImageNameAndExtension(flavorDTO.title(), flavorDTO.image());
        String savePath = uploadDir.replace(uploadDir, "/resources/static/flavors/" + imageName);
        saveImage(flavorDTO.image(), uploadDir + "/flavors", flavorDTO.title());


        //Cria o objeto FlavorEntity para salvar no banco de dados
        FlavorEntity flavor = new FlavorEntity(
                flavorDTO.title(),
                flavorDTO.description(),
                flavorDTO.price(),
                savePath
        );
        flavorRepository.save(flavor);
    }

    public FlavorEntity getFlavorById(Integer id) {
        return flavorRepository.findById(id).orElse(null);
    }

    public FlavorEntity getFlavorByTitle(String title) {
        if(title == null || title.isEmpty()) {
            throw new FlavorException("Title cannot be null or empty");
        }
        FlavorEntity flavor = flavorRepository.findByTitle(title);
        if(flavor != null) {
            flavor.setImageUrl(
                    //Altera o caminho da imagem removendo o root e retornando a url correta e de forma segura
                    flavor.getImageUrl().replace(uploadDir, "static")
            );
        }
        return flavor;
    }

    public List<FlavorEntity> getAllFlavors() {
        List<FlavorEntity> flavors = flavorRepository.findAll();
        if(flavors.isEmpty()) {
            throw new FlavorException("No flavors found");
        }

        return flavors;
    }

    //TODO: Conferir se é necessário implementar mais alguma lógica de validação
    public void deleteFlavor(Integer id) throws IOException {
        if(id == null || id <= 0) {
            throw new FlavorException("Ivalid id");
        }
        FlavorEntity flavor = getFlavorById(id);
        if(flavor == null) {
            throw new FlavorException("Flavor not found");
        }
        flavorRepository.delete(flavor);
        deleteImage(rootDir, flavor.getImageUrl());
    }

    public void updateFlavor(Integer id, FlavorUpdateRequestDTO flavorUpdateRequestDTO) throws IOException {
        if(!flavorRepository.existsById(id)) {
            throw new FlavorException("Flavor not found");
        }

        FlavorEntity flavor = getFlavorById(id);
        String savePath;
        //TODO: Criar método para alterar apenas o nome da imagem caso o usuário não tenha alterado a imagem na requisição
        if(flavorUpdateRequestDTO.image() != null) {
            String imageName = validateImageNameAndExtension(flavorUpdateRequestDTO.title(), flavorUpdateRequestDTO.image());
            savePath = uploadDir.replace(uploadDir, "resources/static/flavors/" + imageName);
            updateImage( rootDir, uploadDir, flavor.getImageUrl(), flavorUpdateRequestDTO.image(), flavorUpdateRequestDTO.title());

        } else {
            savePath = flavor.getImageUrl();
        }

        flavorRepository.findById(flavorUpdateRequestDTO.id())
                .ifPresent(newFlavor -> {
                    newFlavor.setTitle(flavorUpdateRequestDTO.title());
                    newFlavor.setDescription(flavorUpdateRequestDTO.description());
                    newFlavor.setPrice(flavorUpdateRequestDTO.price());
                    newFlavor.setImageUrl(savePath);
                    flavorRepository.save(newFlavor);
                });
    }
}

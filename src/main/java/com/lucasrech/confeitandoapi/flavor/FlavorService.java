package com.lucasrech.confeitandoapi.flavor;

import com.lucasrech.confeitandoapi.exceptions.FlavorException;
import com.lucasrech.confeitandoapi.flavor.dtos.FlavorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import static com.lucasrech.confeitandoapi.utils.ImageUtils.saveImage;
import static com.lucasrech.confeitandoapi.utils.ImageUtils.validateImageNameAndExtension;

@Service
public class FlavorService {


    //TODO: Criar tratamento de exceções para os métodos
    @Value("${upload.dir}")
    private String uploadDir;

    private final FlavorRepository flavorRepository;

    @Autowired
    public FlavorService(FlavorRepository flavorRepository) {
        this.flavorRepository = flavorRepository;
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
    public void deleteFlavor(Integer id) {
        if(id == null || id <= 0) {
            throw new FlavorException("Ivalid id");
        }
        FlavorEntity flavor = getFlavorById(id);
        if(flavor == null) {
            throw new FlavorException("Flavor not found");
        }
        flavorRepository.delete(flavor);
    }

    public FlavorEntity updateFlavor(Integer id, FlavorEntity updatedFlavor) {
        if (flavorRepository.existsById(id)) {
            updatedFlavor.setId(id);
            return flavorRepository.save(updatedFlavor);
        }
        return null;
    }
}

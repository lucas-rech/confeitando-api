package com.lucasrech.confeitandoapi.flavor;

import com.lucasrech.confeitandoapi.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.lucasrech.confeitandoapi.utils.ImageUtils.saveImage;
import static com.lucasrech.confeitandoapi.utils.ImageUtils.saveMultipleImages;

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
    TODO: Criar um método para verificar se o arquivo é uma imagem válida
    TODO: Limitar o tamanho do arquivo e formato (jpg, png, etc.)
    TODO: Criar um método para deletar a imagem do servidor quando o sabor for deletado
    TODO: Criar um método para atualizar a imagem do sabor
    TODO: Alterar variável de ambiente do caminhho de upload quando for criar o serviço
     */

    public void createFlavor(FlavorDTO flavorDTO) throws IOException {
        if(getFlavorByTitle(flavorDTO.title()) != null) {
            throw new IllegalArgumentException("Flavor with this title already exists");
        }

        //Salva a imagem
        String imagePath = saveImage(flavorDTO.image(), uploadDir);


        //Cria o objeto FlavorEntity
        FlavorEntity flavor = new FlavorEntity(
                flavorDTO.title(),
                flavorDTO.description(),
                flavorDTO.price(),
                imagePath
        );
        flavorRepository.save(flavor);
    }

    public FlavorEntity getFlavorById(Integer id) {
        return flavorRepository.findById(id).orElse(null);
    }

    public FlavorEntity getFlavorByTitle(String title) {
        if(title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
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

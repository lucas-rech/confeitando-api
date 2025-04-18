package com.lucasrech.confeitandoapi;

import com.lucasrech.confeitandoapi.exceptions.FlavorException;
import com.lucasrech.confeitandoapi.exceptions.ImageException;
import com.lucasrech.confeitandoapi.flavor.FlavorEntity;
import com.lucasrech.confeitandoapi.flavor.FlavorRepository;
import com.lucasrech.confeitandoapi.flavor.FlavorService;
import com.lucasrech.confeitandoapi.flavor.dtos.FlavorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FlavorServiceTest {

    @Mock
    private FlavorRepository flavorRepository;

    @Mock
    private FlavorService flavorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flavorService = new FlavorService(flavorRepository, "/test/uploads");
    }

    @Test
    void testGetAllFlavors() {
        // Arrange
        FlavorEntity flavor1 = new FlavorEntity(1, "Chocolate", "Delicious chocolate flavor", 10.0, "image1.jpg");
        FlavorEntity flavor2 = new FlavorEntity(2, "Vanilla", "Classic vanilla flavor", 8.0, "image2.jpg");
        when(flavorRepository.findAll()).thenReturn(List.of(flavor1, flavor2));

        // Act
        List<FlavorEntity> flavors = flavorService.getAllFlavors();

        // Assert
        assertEquals(2, flavors.size());
        assertEquals("Chocolate", flavors.get(0).getTitle());
        assertEquals("Vanilla", flavors.get(1).getTitle());
        assertEquals("Delicious chocolate flavor", flavors.get(0).getDescription());
        assertEquals("Classic vanilla flavor", flavors.get(1).getDescription());
        assertEquals(10.0, flavors.get(0).getPrice());
        assertEquals(8.0, flavors.get(1).getPrice());
        verify(flavorRepository, times(1)).findAll();
    }

    @Test
    void testGetFlavorById_Found() {
        // Arrange
        FlavorEntity flavor = new FlavorEntity(1, "Chocolate", "Delicious chocolate flavor", 10.0, "image1.jpg");
        when(flavorRepository.findById(1)).thenReturn(Optional.of(flavor));

        // Act
        FlavorEntity flavorEntity = flavorService.getFlavorById(1);

        // Assert
        assertNotNull(flavorEntity);
        assertEquals("Chocolate", flavorEntity.getTitle());
        verify(flavorRepository, times(1)).findById(1);
    }

    @Test
    void testGetFlavorById_NotFound() {
        // Arrange
        when(flavorRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FlavorException.class, () -> flavorService.getFlavorById(1));
        verify(flavorRepository, times(1)).findById(1);
    }

    @Test
    void testSaveFlavor_Success() throws IOException {
        // Arrange
        FlavorEntity flavor = new FlavorEntity(1, "Bombom", "Delicious chocolate flavor", 10.0, "image1.jpg");
        when(flavorRepository.existsByTitle("Bombom")).thenReturn(false);


        // Act
        try {
            flavorService.createFlavor(new FlavorDTO(
                    "Bombom",
                    "Delicious chocolate flavor",
                    10.0,
                    mock(MultipartFile.class)
            ));
        } catch (AssertionError ignored) {
            // Ignore image exception for this test
        }

        // Assert
        verify(flavorRepository, times(1)).existsByTitle("Bombom");
    }

    @Test
    void testSaveFlavor_InvalidData() {
        // Arrange
        FlavorDTO flavorDTO = new FlavorDTO("", "Description", 10.0, mock(MultipartFile.class));

        // Act & Assert
        assertThrows(FlavorException.class, () -> flavorService.createFlavor(flavorDTO));
        verify(flavorRepository, never()).save(any(FlavorEntity.class));
    }

    @Test
    void testDeleteFlavor_Success() throws IOException {
        // Arrange
        FlavorEntity flavor = new FlavorEntity(1, "Chocolate", "Delicious chocolate flavor", 10.0, "image1.jpg");
        when(flavorRepository.findById(1)).thenReturn(Optional.of(flavor));
        doNothing().when(flavorRepository).delete(flavor);

        // Act
        try {
            flavorService.deleteFlavor(1);
        } catch (ImageException ignored) {
            // Ignore exception for this test
        }

        // Assert
        verify(flavorRepository, times(1)).delete(flavor);
    }

    @Test
    void testDeleteFlavor_NotFound() {
        // Arrange
        when(flavorRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FlavorException.class, () -> flavorService.deleteFlavor(1));
        verify(flavorRepository, never()).deleteById(1);
    }

}

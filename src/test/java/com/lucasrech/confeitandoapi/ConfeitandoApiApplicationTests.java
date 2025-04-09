package com.lucasrech.confeitandoapi;

import com.lucasrech.confeitandoapi.flavor.FlavorRepository;
import com.lucasrech.confeitandoapi.flavor.FlavorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.lucasrech.confeitandoapi.utils.ImageUtils.saveImage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class ConfeitandoApiApplicationTests {
    @Mock
    private MultipartFile multipartFile;
    private FlavorService flavorService;
    @Mock
    private FlavorRepository flavorRepository;

    private final String uploadDir = "/Users/lucas/Documents/Projects/confeitando-api/src/test/uploads";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        flavorService = new FlavorService(flavorRepository);
    }

    @Test
    public void testSaveImage_FileIsEmpty() {
        when(multipartFile.isEmpty()).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                saveImage(multipartFile, uploadDir, ""));

        assertEquals("File is empty", exception.getMessage());
    }

    @Test
    public void testSaveImage_Success() throws IOException {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test-image.jpg");
        InputStream inputStream = mock(InputStream.class);
        when(multipartFile.getInputStream()).thenReturn(inputStream);

        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);

        String result = saveImage(multipartFile, uploadDir, "test-image");

        assertEquals(uploadPath.resolve("test-image.jpg").toString(), result);
        verify(multipartFile, times(1)).getInputStream();
    }

    @Test
    public void testSaveFlavor_Success() {
    }

    //CASO ALGUM DOS VALORES INFORMADOS SEJA NULO OU VAZIO, NÃO DEVE SALVAR A IMAGEM
    @Test
    public void testSaveFlavor_EmptyTitle() {}

    @Test
    public void testSaveFlavor_EmptyDescription() {}

    @Test
    public void testSaveFlavor_EmptyPrice() {}

    @Test
    public void testSaveFlavor_EmptyImage() {}
}

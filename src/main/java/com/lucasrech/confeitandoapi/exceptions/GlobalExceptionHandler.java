package com.lucasrech.confeitandoapi.exceptions;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FlavorException.class)
    public ResponseEntity<ErrorDTO> handleFlavorException(FlavorException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorDTO(ex.getMessage())
        );
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ErrorDTO> handleImageException(ImageException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorDTO(ex.getMessage())
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDTO> handleIOException(IOException ex) {
        return ResponseEntity.internalServerError().body(
                new ErrorDTO("Internal server error: " + ex.getMessage())
        );
    }
}

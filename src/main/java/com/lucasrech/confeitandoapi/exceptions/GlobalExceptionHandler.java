package com.lucasrech.confeitandoapi.exceptions;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}

package com.hytz.habitos_backend.exception;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> gestionarErroresDeValidacion(MethodArgumentNotValidException ex) {

        // 1. Crear un Map vacío donde guardaremos campo -> mensaje
        Map<String, String> errores = new HashMap<>();

        // 2. Extraer los errores de la excepción 'ex'
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField(); // Nombre del atributo (ej: "email")
            String mensaje = error.getDefaultMessage();     // Mensaje definido en @Email o @NotBlank

            errores.put(campo, mensaje);
        });

        // 3. Retornar el mapa con el status HTTP 400
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> gestionarErroresDeNoEncontrado(ResourceNotFoundException ex) {

        Map<String, String> errores = new HashMap<>();

        errores.put("Mensaje", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errores);
    }
}

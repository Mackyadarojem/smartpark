package com.smartpark.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String STATUS = "status";
    public static final String MESSAGE = "message";

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<?> handleInvalidData(
            InvalidDataException ex) {

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        STATUS, 400,
                        MESSAGE, ex.getMessage()
                ));
    }
}
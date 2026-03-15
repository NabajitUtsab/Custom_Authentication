package com.example.Custom_Authentication.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String,String>> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest().
                body(Map.of("message", "Nothing found"));
    }
}

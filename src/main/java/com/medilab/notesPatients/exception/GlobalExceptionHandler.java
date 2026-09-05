package com.medilab.notesPatients.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors()
                               .forEach(err-> errors.put(err.getField(), err.getDefaultMessage()));
        
        
        return ResponseEntity.badRequest().body(Map.of(
                "message", "validation failed",
                "errors", errors));
    }
    
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", ex.getMessage()));
    }
}

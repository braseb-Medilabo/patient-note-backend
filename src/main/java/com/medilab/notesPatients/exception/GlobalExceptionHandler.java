package com.medilab.notesPatients.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.medilab.notesPatients.controllers.NoteController;


@RestControllerAdvice
public class GlobalExceptionHandler {
    
    Logger log = LoggerFactory.getLogger(NoteController.class);
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors()
                               .forEach(err-> errors.put(err.getField(), err.getDefaultMessage()));
        
        String messageError = "validation failed";
        log.error(messageError, ex);
        return ResponseEntity.badRequest().body(new MessageError(messageError, errors));
    }
    
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception ex){
        log.error("internal error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageError("internal error"));
    }
}

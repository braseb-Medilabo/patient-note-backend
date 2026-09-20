package com.medilab.notesPatients.exception;

import java.util.Map;


public class MessageError {
    private String message;
    private Map<String, String> errors;
    
    public MessageError(String message, Map<String, String> errors) {
        this.errors = errors;
        this.message = message;
    }
    
    public MessageError(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}

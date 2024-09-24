package com.hoodie.otti.exception.ott;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OttGlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}

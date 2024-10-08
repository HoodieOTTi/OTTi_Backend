package com.hoodie.otti.exception.profile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalExceptions(Exception ex, WebRequest request) {
        String errorResponse = "내부 서버 오류: " + ex.getMessage();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserProfileNotFoundException.class)
    public ResponseEntity<String> handleUserProfileNotFoundException(UserProfileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errorMessages = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessages.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errorMessages.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        String errorResponse = "요청 인자가 유효하지 않습니다: " + String.join(", ", errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

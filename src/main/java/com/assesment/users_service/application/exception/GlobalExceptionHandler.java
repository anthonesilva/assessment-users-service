package com.assesment.users_service.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({HttpMessageConversionException.class, MethodArgumentNotValidException.class, IllegalArgumentException.class, EntityNotFoundException.class})
    public ResponseEntity<?> handleParsingException(Exception ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUniqueConstraintException(UniqueConstraintException ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(customError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        CustomError customError = new CustomError(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(customError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

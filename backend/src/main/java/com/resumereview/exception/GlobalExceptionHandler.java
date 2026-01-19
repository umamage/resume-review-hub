package com.resumereview.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        log.error("Resource not found: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "RESOURCE_NOT_FOUND",
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        log.error("Invalid argument: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "INVALID_ARGUMENT",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        log.error("Internal server error: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Internal server error",
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

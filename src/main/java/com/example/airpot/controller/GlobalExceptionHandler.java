package com.example.airpot.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


/**
 * Global exception handler for the application.
 * Handles various types of exceptions and returns appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles IllegalArgumentException thrown by business logic validation.
     *
     * @param ex the IllegalArgumentException that was thrown
     * @return ResponseEntity with error details and BAD_REQUEST status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex){
        log.error("Business rule violation: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(BAD_REQUEST.value())
                .error("Business Rule Violation")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);

    }

    /**
     * Handles validation errors from request body validation.
     *
     * @param ex the MethodArgumentNotValidException containing validation errors
     * @return ResponseEntity with validation error details and BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex){
        log.error("Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(BAD_REQUEST.value())
                .error("Validation Error")
                .message("Invalid input data: " + errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }




    /**
     * Handles all other unhandled exceptions as a fallback.
     *
     * @param ex the generic Exception that was thrown
     * @return ResponseEntity with generic error message and INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex){
        log.error("An unexpected error occurred: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("An unexpected error occurred")
                .message( ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    /**
     * Standard error response structure for API errors.
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class ErrorResponse{
        private int status;
        private String error;
        private String message;
        private LocalDateTime timestamp;
    }
}

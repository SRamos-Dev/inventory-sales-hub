package com.github.inventorysaleshub.exception;

import com.github.inventorysaleshub.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // --- Handle validation errors (DTOs with @Valid) ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ApiResponseDTO<Map<String, String>> response =
                new ApiResponseDTO<>(false, "Validation error", errors);

        return ResponseEntity.badRequest().body(response);
    }

    // --- Handle illegal arguments (e.g. invalid login, missing role, etc.) ---
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponseDTO<String> response =
                new ApiResponseDTO<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // --- Handle all other exceptions ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<String>> handleGeneralException(Exception ex) {
        ApiResponseDTO<String> response =
                new ApiResponseDTO<>(false, "Internal server error", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


package com.krowdless.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.krowdless.dto.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler  {

    // 1. Handle your own “not found” or domain errors
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        ApiResponse<Void> body = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
    
    // 2. Custom: Business rule / invalid state
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }
    
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbidden(ForbiddenException ex) {
        log.warn("Forbidden: {}", ex.getMessage());
        ApiResponse<Void> body = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }
    
    // 4. Validation errors on @RequestBody parameters
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        // 1. Extract all messages
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());

        // 2. Combine into one string
        String combinedMessage = String.join("; ", errorMessages);

        // 3. Build response with data = null
        ApiResponse<Object> response = new ApiResponse<>(
            false,
            combinedMessage,
            null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 5. Constraint violations on request params / path variables
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Entity constraint violation: {}", ex.getMessage());

        // 1. Extract all messages
        List<String> errorMessages = ex.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());

        // 2. Combine into one string
        String combinedMessage = String.join("; ", errorMessages);

        // 3. Build response with data = null
        ApiResponse<Object> response = new ApiResponse<>(
            false,
            combinedMessage,
            null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

     
    // 6. Catch-all for any other uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllUnhandledExceptions(Exception ex) {
        log.error("Unhandled exception", ex);
        ApiResponse<Void> body = new ApiResponse<>(
                false,
                "An unexpected error occurred. Please try again later." +ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
    
    // 7. Static resource not found (e.g., missing files)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFound(NoResourceFoundException ex) {
        log.warn("Static resource not found: {}", ex.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(false, "Static resource not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
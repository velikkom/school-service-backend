package com.schoolservice.school_service_backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =============================
       BUSINESS EXCEPTION
    ============================== */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    /* =============================
       VALIDATION ERROR
    ============================== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error");

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        message,
                        LocalDateTime.now()
                ));
    }

    /* =============================
       ACCESS DENIED (403)
    ============================== */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(
                        HttpStatus.FORBIDDEN.value(),
                        "Access denied",
                        LocalDateTime.now()
                ));
    }

    /* =============================
       GENERIC EXCEPTION (500)
    ============================== */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Unexpected error occurred",
                        LocalDateTime.now()
                ));
    }
}

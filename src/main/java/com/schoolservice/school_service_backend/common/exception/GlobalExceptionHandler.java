package com.schoolservice.school_service_backend.common.exception;

import com.schoolservice.school_service_backend.common.response.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =============================
       RESOURCE NOT FOUND (404)
    ============================== */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleNotFound(
            ResourceNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseWrapper.fail(ex.getMessage()));
    }

    /* =============================
       BUSINESS EXCEPTION (400)
    ============================== */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleBusiness(
            BusinessException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseWrapper.fail(ex.getMessage()));
    }

    /* =============================
       VALIDATION ERROR (400)
    ============================== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseWrapper.fail(message));
    }

    /* =============================
       UNAUTHORIZED (401)
    ============================== */
    @ExceptionHandler({
            org.springframework.security.core.userdetails.UsernameNotFoundException.class,
            org.springframework.security.authentication.BadCredentialsException.class
    })
    public ResponseEntity<ResponseWrapper<Void>> handleUnauthorized(
            RuntimeException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ResponseWrapper.fail(ex.getMessage()));
    }

    /* =============================
       FORBIDDEN (403)
    ============================== */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseWrapper<Void>> handleAccessDenied(
            AccessDeniedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ResponseWrapper.fail("Access denied"));
    }

    /* =============================
       GENERIC ERROR (500) – TEK TANE
    ============================== */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ResponseWrapper<Void>> handleGeneric(
//            Exception ex
//    ) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ResponseWrapper.fail("Unexpected error occurred"));
//    }

    @ExceptionHandler(Exception.class)
    public ResponseWrapper handleGeneric(Exception ex) {
        return ResponseWrapper.error(ex.getMessage());
    }
}

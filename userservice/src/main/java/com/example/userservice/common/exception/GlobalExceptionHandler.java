package com.example.userservice.common.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.example.userservice.common.constants.ConstantsUtil;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final String RETRY_AFTER = "retryAfterSeconds";
    private final String ERRORS = "errors";

    // 전역 예외
    @ExceptionHandler(exception = {Exception.class})
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> body = Map.of(
            ConstantsUtil.RETURN_MESSAGE, "시스템 오류가 발생했습니다."
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 일시적인 예외
    @ExceptionHandler(exception = {TemporaryException.class})
    public ResponseEntity<Map<String, Object>> handleTemporaryException(TemporaryException e) {
        Map<String, Object> body = Map.of(
            ConstantsUtil.RETURN_MESSAGE, e.getMessage(),
            RETRY_AFTER, e.getRetryAfterSeconds()
        );

        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // EntityNotFoundException 예외
    @ExceptionHandler(exception = {EntityNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException e) {
        Map<String, Object> body = Map.of(
            ConstantsUtil.RETURN_MESSAGE, e.getMessage()
        );

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Validation 예외
    @ExceptionHandler(exception = {MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        Map<String, Object> body = Map.of(
            ConstantsUtil.RETURN_MESSAGE, "입력값이 올바르지 않습니다.",
            ERRORS, errors
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // BusinessException 예외
    @ExceptionHandler(exception = {BusinessException.class})
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {
        Map<String, Object> body = Map.of(
            ConstantsUtil.RETURN_MESSAGE, e.getMessage(),
            ERRORS, e.getErrors()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(ConstantsUtil.RETURN_MESSAGE, ex.getReason());

        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Map<String, Object>> handleLockedException(LockedException ex) {
        Map<String, Object> errorResponse = Map.of(
            ConstantsUtil.RETURN_MESSAGE, "사용자의 계정이 잠겼습니다.",
            "errorCode", "ACCOUNT_LOCKED"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("error", "Invalid username or password");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}

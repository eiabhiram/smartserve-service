package com.smartops.smartserve.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class SSGlobalExceptionHandler {

    @ExceptionHandler(SSBusinessException.class)
    public ResponseEntity<SSErrorResponse> handleBusinessException(
            SSBusinessException ex, WebRequest request) {
        
        log.warn("Business validation failed: {}", ex.getMessage());
        
        SSErrorResponse errorResponse = new SSErrorResponse(
            "VALIDATION_ERROR",
            ex.getMessage(),
            ex.getArgs()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SSErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        
        log.error("Unexpected error occurred: ", ex);
        
        SSErrorResponse errorResponse = new SSErrorResponse(
            "INTERNAL_ERROR",
            "An unexpected error occurred",
            null
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

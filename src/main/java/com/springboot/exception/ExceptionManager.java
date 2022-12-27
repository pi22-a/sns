package com.springboot.exception;

import com.springboot.domain.ErrorResponse;
import com.springboot.domain.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> hospitalReviewExceptionHandler(ApplicationException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(errorResponse));
    }
}
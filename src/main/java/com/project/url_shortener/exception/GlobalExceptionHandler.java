package com.project.url_shortener.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse>handleApiException(
            ApiException ex,
            HttpServletRequest request
    ){
        ErrorResponse response=new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatus().value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>handleGenericException(
            Exception ex,
            HttpServletRequest request
    ){
        ErrorResponse response =new ErrorResponse(
                LocalDateTime.now(),
                500,
                "Internal Server Error",
                request.getRequestURI()
        );
        return ResponseEntity.internalServerError().body(response);
    }
}

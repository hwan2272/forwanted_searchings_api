package com.hwan2272.forwanted.searchingsapi.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity exceptionAll(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }
}

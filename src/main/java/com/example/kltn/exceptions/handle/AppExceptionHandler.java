package com.example.kltn.exceptions.handle;

import com.example.kltn.dto.ExceptionResq;
import com.example.kltn.exceptions.AuthException;
import com.example.kltn.exceptions.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(UserException.class)
    private ResponseEntity<?> userException(UserException e){
        return ResponseEntity.status(e.getCode())
                .body(new ExceptionResq(e.getCode(),e.getMessage()));
    }
    @ExceptionHandler(AuthException.class)
    private ResponseEntity<?> authException(AuthException e){
        return ResponseEntity.status(e.getCode())
                .body(new ExceptionResq(e.getCode(),e.getMessage()));
    }
}

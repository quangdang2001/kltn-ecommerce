package com.example.kltn.exceptions.handle;

import com.example.kltn.dto.ExceptionResq;
import com.example.kltn.exceptions.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(UserException.class)
    private ResponseEntity<?> userException(UserException e) {
        return ResponseEntity.status(e.getCode())
                .body(new ExceptionResq(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    private ResponseEntity<?> authException(AuthException e) {
        return ResponseEntity.status(e.getCode())
                .body(new ExceptionResq(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(AppException.class)
    private ResponseEntity<?> appException(AppException e) {
        return ResponseEntity.status(e.getCode())
                .body(new ExceptionResq(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    private ResponseEntity<?> duplicateException(DuplicateKeyException e) {
        return ResponseEntity.status(400)
                .body(new ExceptionResq(400, e.getMessage()));
    }
    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<?> notFoundException(NotFoundException e) {
        return ResponseEntity.status(404)
                .body(new ExceptionResq(404, e.getMessage()));
    }

    @ExceptionHandler(PaymentException.class)
    private ResponseEntity<?> paymentException(PaymentException e) {
        return ResponseEntity.status(404)
                .body(new ExceptionResq(400, e.getMessage()));
    }
}

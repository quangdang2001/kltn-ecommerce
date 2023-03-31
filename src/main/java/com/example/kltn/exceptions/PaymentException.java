package com.example.kltn.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentException extends RuntimeException {
    private int code;
    private String message;
}

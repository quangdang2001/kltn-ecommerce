package com.example.kltn.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OutOfStockException extends RuntimeException {
    private String message;
}

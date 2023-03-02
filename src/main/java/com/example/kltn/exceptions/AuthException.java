package com.example.kltn.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class AuthException extends RuntimeException{
    private int code;
    private String message;
}

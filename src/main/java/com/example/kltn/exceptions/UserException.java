package com.example.kltn.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserException extends RuntimeException{
    private int code;
    private String message;
}

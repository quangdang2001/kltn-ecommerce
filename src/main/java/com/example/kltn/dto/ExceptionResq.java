package com.example.kltn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResq {
    private int status;
    private Object message;
}

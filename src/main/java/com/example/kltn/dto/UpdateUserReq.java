package com.example.kltn.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserReq {
    private String gender;
    private String phone;
    private String name;
}

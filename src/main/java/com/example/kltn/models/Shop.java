package com.example.kltn.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shop")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    private String province;
    private String district;
    private String fullAddress;
    private String phone;
}

package com.example.kltn.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private String id;
    private String name; // example: iPhone 14 Pro Max 256GB
    private String slug;
    private String description;
    private String video;
    private double rate = 0;
    private int countRate = 0;
    private int view = 0;
    private boolean enable = true;
    private Double installment;
    private List<ProductOption> productOptions;
    @DocumentReference
    private Category category;
    @DocumentReference
    private Category subcategory;
    @DocumentReference
    private Manufacturer manufacturer;
    private List<GroupSpecification> specifications;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Getter
    @Setter
    public static class ProductOption {
        private String optionName; // example: 512GB-Xanh
        private BigDecimal marketPrice;
        private int promotion = 0;
        private String color;
        private String key;
        private int sold = 0;
        private List<String> pictures;
        private String thumbnail;

    }
}

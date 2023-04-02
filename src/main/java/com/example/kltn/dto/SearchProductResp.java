package com.example.kltn.dto;

import com.example.kltn.models.Product;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.util.List;

public class SearchProductResp {
    private String id;
    private String name;
    private String view;
    private double rate = 0;
    private int countRate = 0;
    private boolean enable;
    private String slug;
    private List<Option> options;

    public static class Option{
        private String id;
        private String optionName; // example: 512GB-Xanh
        private String fullName;
        private BigDecimal marketPrice;
        private BigDecimal salePrice;
        private int promotion = 0;
        private String color;
        @Indexed(unique = true)
        private String key;
        private String thumbnail;
    }
}

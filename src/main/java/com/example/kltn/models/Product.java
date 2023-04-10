package com.example.kltn.models;

import com.example.kltn.utils.MoneyConvert;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
public class Product {
    private String id;
    private String name; // example: iPhone 14 Pro Max 256GB
    @Indexed(unique = true)
    private String slug;
    private String description;
    private String video;
    private double rate = 0;
    private int countRate = 0;
    private int view = 0;
    private String thumbnail;
    private String saleBanner;
    private boolean enable = true;
    private Double installment;
    private List<GroupSpecification> specifications;
    private List<Parameter> parameters;

    private BigDecimal bestPrice;
    private int bestPromotion;

    @DocumentReference
    private List<ProductOption> productOptions;
    @DocumentReference(lookup = "{'slug': ?#{#target}}")
    private Category category;
    @DocumentReference(lookup = "{'slug': ?#{#target}}")
    private Manufacturer manufacturer;
    @DocumentReference
    private List<Comment> comments;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    private LocalDateTime createAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    private LocalDateTime updateAt;

    @Document(collection = "product_option")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductOption {
        private String id;
        private String optionName; // example: 512GB-Xanh
        private String fullName;
        private BigDecimal marketPrice;
        private int promotion = 0;
        private String color;
        @Indexed(unique = true)
        private String key;
        private List<String> pictures;
        private BigDecimal salePrice;

        @JsonIgnore
        @DocumentReference
        private Product product;
    }

    @Getter
    @Setter
    @Document(collection = "product_shop")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductShop {
        private String id;
        @DocumentReference
        private ProductOption productOption;
        @DocumentReference
        private Shop shop;
        private int quantity = 0;
    }

    @Getter
    @Setter
    public static class Parameter {
        private String value;
        private String key;
    }

}

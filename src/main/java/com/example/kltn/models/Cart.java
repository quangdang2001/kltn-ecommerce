package com.example.kltn.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

@Document()
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private String id;
    @Indexed(unique = true)
    private String userId;
    private List<CartItem> items;

    @Getter
    @Setter
    public static class CartItem{
        @DocumentReference
        private Product.ProductOption productOption;
        private int qty = 0;
        @LastModifiedDate
        private LocalDateTime updateAt;
    }
}



package com.example.kltn.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "shop_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(def = "{'productOption': 1, 'shop': 1}", unique = true)
public class ShopProduct {
    private String id;
    @DocumentReference(lazy = true)
    private Product.ProductOption productOption;
    @DocumentReference(lazy = true)
    private Shop shop;
    private int quantity = 9999; // quantity of product in stock
    private int sold = 0;
}

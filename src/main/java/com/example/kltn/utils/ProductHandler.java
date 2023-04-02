package com.example.kltn.utils;

import com.example.kltn.models.Product;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

public class ProductHandler {
    public static BigDecimal getBestPrice(Product product){
        var productOps = product.getProductOptions();
        return productOps.stream().min(Comparator.comparing(Product.ProductOption::getSalePrice)).get().getSalePrice();

    }
    public static int getBestPromotion(Product product){
        var productOps = product.getProductOptions();
        return productOps.stream().max(Comparator.comparing(Product.ProductOption::getPromotion)).get().getPromotion();
    }

}

package com.example.kltn.dto;

import com.example.kltn.models.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;


@Data
public class OrderReq {
    @NotEmpty(message = "Paymend method not empty")
    private String paymentMethod;
    private String discountCode;
    private String shopSelectedId;
    private String deliveryAddress;
    private String description;
    private String differentReceiverName;
    private String differentReceiverPhone;
    @NotNull(message = "Item not empty")
    private List<Items> items;
    @Data
    public static class Items{
        @NotBlank(message = "Product ID may not be blank")
        private String productOptionId;
        @NotBlank(message = "quantity may not be blank")
        private int quantity;

        @JsonIgnore
        private BigDecimal price;
        @JsonIgnore
        private Product.ProductOption productOption;
    }
}

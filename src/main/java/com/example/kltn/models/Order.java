package com.example.kltn.models;


import com.fasterxml.jackson.annotation.JsonFormat;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
@Document(collection = "order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    @Id
    private String id;
    private String state;
    private LocalDateTime deliveryDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt = LocalDateTime.now();
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    private LocalDateTime updateAt;
    private String paypalToken;
    private OrderDetail orderdetail;

    private List<OrderItem> orderItems;
    @DocumentReference
    private User orderUser;

    private PaymentMethod paymentMethod;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDetail {
        private Shop shopSelected;
        private String deliveryAddress;
        private String discountCode;
        private BigDecimal totalPrice = BigDecimal.valueOf(0);
        private Integer quantity = 0;
        private String description;
        private String differentReceiverName;
        private String differentReceiverPhone;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {
        @NotNull
        private int quantity = 0;
        @NotNull
        private BigDecimal price = BigDecimal.ZERO;
        @DocumentReference
        private Product.ProductOption productOption;
        @DocumentReference
        private Category category;
        @DocumentReference
        private Manufacturer manufacturer;
        @DocumentReference
        private Product product;
    }
}

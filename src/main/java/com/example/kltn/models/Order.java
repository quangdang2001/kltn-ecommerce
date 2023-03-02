package com.example.kltn.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Document(collection = "order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private Long id;
    private String state;
    private LocalDateTime deliveryDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime updateAt;
    private User orderUser;
    private String paypalToken;
    private OrderDetail orderdetail;
    private List<OrderItem> orderItems;
    @DocumentReference
    private PaymentMethod paymentMethod;

    @Getter
    @Setter
    public class OrderDetail {
        private String deliveryAddress;
        private String discountCode;
        private BigDecimal totalPrice;
        private Integer quantity;
        private String description;
        private String differentReceiverName;
        private String differentReceiverPhone;
    }

    @Getter
    @Setter
    public class OrderItem {
        @NotNull
        private int quantity;
        @NotNull
        private BigDecimal price;
        @DocumentReference
        private Product.ProductOption productOption;
    }
}

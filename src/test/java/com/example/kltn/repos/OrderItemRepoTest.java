package com.example.kltn.repos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DataMongoTest
class OrderItemRepoTest {

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Test
    void countByOrder_Orderdetail_ShopSelected_IdAndProductOption_IdAndOrder_CreateAtDate() {
    }
}
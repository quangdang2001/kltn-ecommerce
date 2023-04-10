package com.example.kltn.repos;

import com.example.kltn.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class ProductRepoTest {

    @Autowired
    ProductRepo productRepo;

    @Test
    void testMethod(){
        productRepo.save(new Product());
        System.out.println(productRepo.findAll());
    }
}
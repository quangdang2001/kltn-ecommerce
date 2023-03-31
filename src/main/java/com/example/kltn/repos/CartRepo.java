package com.example.kltn.repos;

import com.example.kltn.models.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CartRepo extends MongoRepository<Cart, String> {
    Cart findCartByUser_Id(String userId);
    void deleteAllByUser_Id(String userId);
}

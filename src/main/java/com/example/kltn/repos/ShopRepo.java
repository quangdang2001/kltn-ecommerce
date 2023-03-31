package com.example.kltn.repos;

import com.example.kltn.models.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShopRepo extends MongoRepository<Shop, String> {
}

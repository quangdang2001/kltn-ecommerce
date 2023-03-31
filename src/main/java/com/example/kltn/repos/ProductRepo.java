package com.example.kltn.repos;

import com.example.kltn.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepo extends MongoRepository<Product, String> {
}

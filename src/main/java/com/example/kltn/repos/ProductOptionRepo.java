package com.example.kltn.repos;

import com.example.kltn.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductOptionRepo extends MongoRepository<Product.ProductOption, String> {
    List<Product.ProductOption> findProductOptionByIdIn(List<String> id);
}

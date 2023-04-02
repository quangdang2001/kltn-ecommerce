package com.example.kltn.repos;

import com.example.kltn.models.Category;
import com.example.kltn.models.Manufacturer;
import com.example.kltn.models.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepo extends MongoRepository<Product, String> {
    @Query(value = "{'name': {'$regex' : ?0, '$options': 'i'}, 'category': {'$regex' : ?1},'manufacturer':  {'$regex' : ?2}}",
    fields = "{'description':  0, 'installment':  0, 'specifications':  0, 'comments':  0}")
    List<Product> searchProducts(String keyword, String cate, String manu, Pageable pageable);
}

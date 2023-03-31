package com.example.kltn.repos;

import com.example.kltn.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepo extends MongoRepository<Category, String> {
}

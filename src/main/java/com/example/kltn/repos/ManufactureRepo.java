package com.example.kltn.repos;

import com.example.kltn.models.Manufacturer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ManufactureRepo extends MongoRepository<Manufacturer, String> {
}

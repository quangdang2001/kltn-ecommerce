package com.example.kltn.repos;

import com.example.kltn.models.PaymentMethod;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentMethodRepo extends MongoRepository<PaymentMethod, String> {
    PaymentMethod findPaymentMethodByName(String name);
}

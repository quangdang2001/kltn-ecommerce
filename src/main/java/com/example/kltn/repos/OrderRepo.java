package com.example.kltn.repos;

import com.example.kltn.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends MongoRepository<Order, String> {
    @Query(value = "{'orderUser': {'id': ?0}}",
            fields = "{\n" +
                    "  'id': 1,\n" +
                    "  'orderUser': {\n" +
                    "    'name': 1\n" +
                    "  },\n" +
                    "  'deliveryDate': 1,\n" +
                    "  'createAt': 1" +
                    ",\n" +
                    "  'orderdetail': 1,\n" +
                    "  'state': 1,\n" +
                    "  'paymentMethod': 1\n" +
                    "}")
    List<Order> findHistoryOrderUser(String userId);


}

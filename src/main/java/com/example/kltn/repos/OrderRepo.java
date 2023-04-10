package com.example.kltn.repos;

import com.example.kltn.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
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



    @Query(value = "{'orderItems': {'productOption.id': ?0}}", count = true)
    long countProductSold(String date);

    @Query(value = "{'orderItems.product': ObjectId(?0), 'orderdetail.shopSelected.id': ObjectId(?1)}")
    List<Order> statisticProductByProductIdAndShopId(ObjectId productId, ObjectId shopId);

    @Query(value = "{'orderItems.product': ObjectId(?0)}")
    List<Order> statisticProductByProductId(ObjectId productId);

    @Query(value = "{'orderItems.category': ObjectId(?0), 'orderdetail.shopSelected.id': ObjectId(?1)}")
    List<Order> statisticProductByCategoryIdAndShopId(ObjectId category, ObjectId shopId);

    @Query(value = "{'orderItems.category': ObjectId(?0)}")
    List<Order> statisticProductByCategoryId(ObjectId category);

    @Query(value = "{'orderItems.manufacturer': ObjectId(?0), 'orderdetail.shopSelected.id': ObjectId(?1)}")
    List<Order> statisticProductByManufacturerIdAndShopId(ObjectId manufacturer, ObjectId shopId);

    @Query(value = "{'orderItems.manufacturer': ObjectId(?0)}")
    List<Order> statisticProductByManufacturerId(ObjectId manufacturer);


}

package com.example.kltn.repos;

import com.example.kltn.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductShopRepo extends MongoRepository<Product.ProductShop, String> {

    @Query(value = "{'productOption':  {'_id':  ?0}, 'shop':  {'province':  ?1}}", fields = "{'productOption': 0}")
    List<Product.ProductShop> findShopAvailableNearBy(String productOptionID, String province);

    Product.ProductShop findProductShopByProductOption_IdAndShop_Id(String productOptionId, String shopId);

    @Query(value = "{'productOption':  {$in : ?0}, 'shop': {'_id':  ?1}}")
    List<Product.ProductShop> findProductShopByProductOptions_IdAndShop_Id(List<String> productOptionId, String shopId);

}

package com.example.kltn.services.common;

import com.example.kltn.models.Product;
import com.example.kltn.models.Shop;

import java.util.List;

public interface ProductSrv {
    Product saveNewProduct(Product productReq);
    Product findProductById(String productId);
    Product updateProduct(Product productReq);


//    List<SearchProductResp> getProductByKeyword(String keyword, Long manufacturerId, Long categoryId, Long subCategoryId, int page, int size);
//
//    List<SearchProductResp> search(String keyword, int page, int size);

    void disableProduct(String productId);

    void updateQuantityProduct(String shopId, String productOptionId, Integer quantity);
}

package com.example.kltn.services.common;

import com.example.kltn.dto.ProductSearchReq;
import com.example.kltn.dto.SearchProductResp;
import com.example.kltn.models.Product;
import com.example.kltn.models.Shop;

import java.util.List;

public interface ProductSrv {
    Product saveNewProduct(Product productReq);
    Product findProductById(String productId);
    Product updateProduct(Product productReq);


    List<Product> getProductByKeyword(ProductSearchReq searchReq);
//
//    List<SearchProductResp> search(String keyword, int page, int size);

    void disableProduct(String productId);

    void updateQuantityProduct(String shopId, String productOptionId, Integer quantity);
}

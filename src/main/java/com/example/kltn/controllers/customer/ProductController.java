package com.example.kltn.controllers.customer;

import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.services.common.ProductSrv;
import com.example.kltn.services.common.iplm.ShopSrvIplm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductSrv productService;
    private final ShopSrvIplm shopIplm;


    @GetMapping("/product/{productId}")
    private ResponseEntity<?> getProductById(@PathVariable String productId){
        var productResp = productService.findProductById(productId);
        if (productResp !=null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",productResp));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(true,"Not Found Product ID",null));
    }

    @GetMapping("/product/shopAvailableNear")
    private ResponseEntity<?> getShopNearBy(@RequestParam String productOptionId){
        var shopNears = shopIplm.findShopNearBy(productOptionId);
        return ResponseEntity.ok(new ResponseDTO(true, "Success", shopNears));
    }
}

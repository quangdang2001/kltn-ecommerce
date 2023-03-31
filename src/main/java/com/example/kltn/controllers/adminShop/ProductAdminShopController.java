package com.example.kltn.controllers.adminShop;

import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.services.common.ProductSrv;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adminShop")
@RequiredArgsConstructor
//@SecurityRequirement(name = "AUTHORIZATION")
public class ProductAdminShopController {
    private final ProductSrv productSrv;
    @PutMapping("/product")
    public ResponseEntity<?> updateQuantityProduct(@RequestParam String shopId,
                                                   @RequestParam String productOptionId,
                                                   @RequestParam Integer quantity
                                                   ){
        productSrv.updateQuantityProduct(shopId, productOptionId, quantity);
        return ResponseEntity.ok(new ResponseDTO(true, "Success", null));
    }
}

package com.example.kltn.controllers.adminSystem;

import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.models.Product;
import com.example.kltn.services.common.ProductSrv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adminSys")
@RequiredArgsConstructor
@Slf4j
public class ProductAdminSysController {
    private final ProductSrv productService;

    @PostMapping("/product")
    private ResponseEntity<?> addProduct(@RequestBody Product productReq){
        Product product= productService.saveNewProduct(productReq);
        if (product !=null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success",product));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false,"Failed",null));
    }

    @PutMapping("/product/disable/{productId}")
    private ResponseEntity<?> disableProduct(@PathVariable String productId){
        productService.disableProduct(productId);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
}

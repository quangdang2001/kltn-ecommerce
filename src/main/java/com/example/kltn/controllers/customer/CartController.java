package com.example.kltn.controllers.customer;

import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.models.Cart;
import com.example.kltn.services.common.CartSrv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
//@SecurityRequirement(name = "AUTHORIZATION")
public class CartController {

    private final CartSrv cartService;

    @GetMapping("/cart")
    private ResponseEntity<?> getCart(){
        Cart cartList = cartService.view();
        if(cartList != null){
            return ResponseEntity.ok(new ResponseDTO(true,"Success", cartList));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(false,"No Item!",null));
        }
    }
    @PostMapping("/cart/add")
    private ResponseEntity<?> addProduct(@RequestParam String productOptionId,
                                         @RequestParam int quantity){

        cartService.save(productOptionId,quantity);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
    @DeleteMapping("/cart/remove")
    public ResponseEntity<?> removeCartItem(@RequestParam String productOptionId){
        cartService.deleteCart(productOptionId);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
    @DeleteMapping("/cart")
    public ResponseEntity<?> deleteAllCartUser(){
        cartService.deleteAllCartUser();
        return ResponseEntity.ok(new ResponseDTO(true,"Success", null));
    }
}

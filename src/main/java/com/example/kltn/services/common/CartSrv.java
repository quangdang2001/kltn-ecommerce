package com.example.kltn.services.common;

import com.example.kltn.models.Cart;

import java.util.List;

public interface CartSrv {
    void save(String productOptionId, int quantity);
    Cart view();
    void deleteCart(String cartID);
    void deleteAllCartUser();
}

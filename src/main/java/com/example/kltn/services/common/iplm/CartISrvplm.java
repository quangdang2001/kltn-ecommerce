package com.example.kltn.services.common.iplm;

import com.example.kltn.models.Cart;
import com.example.kltn.repos.CartRepo;
import com.example.kltn.repos.ProductOptionRepo;
import com.example.kltn.repos.UserRepo;
import com.example.kltn.services.common.CartSrv;
import com.example.kltn.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartISrvplm implements CartSrv {
    private final CartRepo cartRepo;
    private final ProductOptionRepo productOptionRepo;
    private final UserRepo userRepo;

    @Override
    public void save(String productOptionId, int quantity) {
        if (quantity == 0) return;
        var cart = cartRepo.findCartByUser_Id(UserUtil.getIdCurrentUser());
        var productOption = productOptionRepo.findById(productOptionId).orElseThrow();
        if (cart == null) {
            var newCart = new Cart();
            newCart.setUser(userRepo.findById(UserUtil.getIdCurrentUser()).orElseThrow());
            newCart.setItems(List.of(new Cart.CartItem(productOption, 0)));
            cartRepo.save(newCart);
        } else {
            boolean checkExits = false;
            for (var item : cart.getItems()) {
                if (item.getProductOption().getId().equals(productOptionId)) {
                    checkExits = true;
                    item.setQty(quantity);
                    break;
                }
            }
            if (!checkExits) {
                cart.getItems().add(new Cart.CartItem(productOption, quantity));
            }
            cartRepo.save(cart);
        }
    }

    @Override
    public Cart view() {
        var cart = cartRepo.findCartByUser_Id(UserUtil.getIdCurrentUser());
        if (cart == null)
            return null;
        cart.setUser(null);
        return cart;
    }

    @Override
    public void deleteCart(String productOptionId) {
        var cart = cartRepo.findCartByUser_Id(UserUtil.getIdCurrentUser());
        cart.getItems().removeIf(cartItem -> cartItem.getProductOption().getId().equals(productOptionId));
        cartRepo.save(cart);
    }

    @Override
    public void deleteAllCartUser() {
        cartRepo.deleteAllByUser_Id(UserUtil.getIdCurrentUser());
    }
}

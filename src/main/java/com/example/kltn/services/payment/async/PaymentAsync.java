package com.example.kltn.services.payment.async;

import com.example.kltn.exceptions.OutOfStockException;
import com.example.kltn.models.Order;
import com.example.kltn.models.Product;
import com.example.kltn.models.Shop;
import com.example.kltn.repos.ProductOptionRepo;
import com.example.kltn.repos.ProductShopRepo;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PaymentAsync {
    private final ProductShopRepo productShopRepo;
    private final int TIME_OUT = 5;

    @Async
    @Synchronized
    public CompletableFuture<Boolean> asyncCheckAndUpdateQuantityProduct(List<Order.OrderItem> orderItems, Shop shop) {
        log.info("async check and update quantity start");
        List<Product.ProductShop> productShops = productShopRepo.findProductShopByShopAndProductOptionIn(
                shop,
                orderItems.stream().map(Order.OrderItem::getProductOption).collect(Collectors.toList())

        );
        productShops.forEach(productShop -> {
            int quantity = orderItems.stream()
                    .filter(orderItem -> orderItem.getProductOption().getId().equals(productShop.getProductOption().getId()))
                    .findFirst().get().getQuantity();
            if (productShop.getQuantity() < quantity) {
                throw new OutOfStockException("Out of stock");
            }
            productShop.setQuantity(productShop.getQuantity() - quantity);
        });

        productShopRepo.saveAll(productShops);
        log.info("async check and update quantity end");
        return CompletableFuture.completedFuture(true);
    }


}

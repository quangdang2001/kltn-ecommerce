package com.example.kltn.services.common;

import com.example.kltn.models.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderUserSrv {
    List<Order> getOrderHistory(final String userId);

    List<Order> searchOrder(final String state, Integer page, Integer size);

    Order findByIdOrder(String id);

    List<Order> findAllOrder(int page, int size);

    Long countOrderByDay(int day, int month, int year);

    BigDecimal countRevenueByDay(int day, int month, int year);

    Order updateStatusOrder(Long orderId, String status);

    Order save(Order orders);
}

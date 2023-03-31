package com.example.kltn.services.common.iplm;

import com.example.kltn.models.Order;
import com.example.kltn.repos.OrderRepo;
import com.example.kltn.services.common.OrderUserSrv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderUserSrvIplm implements OrderUserSrv {
    private final OrderRepo orderRepo;

    @Override
    public List<Order> getOrderHistory(String userId) {
        return orderRepo.findHistoryOrderUser(userId);
    }

    @Override
    public List<Order> searchOrder(String state, Integer page, Integer size) {
        return null;
    }

    @Override
    public Order findByIdOrder(String id) {
        return orderRepo.findById(id).orElseThrow();
    }

    @Override
    public List<Order> findAllOrder(int page, int size) {
        return null;
    }

    @Override
    public Long countOrderByDay(int day, int month, int year) {
        return null;
    }

    @Override
    public BigDecimal countRevenueByDay(int day, int month, int year) {
        return null;
    }

    @Override
    public Order updateStatusOrder(Long orderId, String status) {
        return null;
    }

    @Override
    public Order save(Order orders) {
        return null;
    }
}

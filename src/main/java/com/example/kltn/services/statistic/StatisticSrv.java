package com.example.kltn.services.statistic;


import com.example.kltn.dto.Statistic;
import com.example.kltn.exceptions.NotFoundException;

import com.example.kltn.models.Order;
import com.example.kltn.repos.OrderRepo;
import com.example.kltn.utils.DateTimeUtil;
import com.example.kltn.utils.MoneyConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticSrv {

    private final OrderRepo orderRepo;

    public Statistic countItemSold(int day, int month, int year, String shopId, String typeRevenue, String typeId) {
        TypeRevenue type = TypeRevenue.getValue(typeRevenue);
        if (type == null) throw new NotFoundException("Type Revenue Not Found!");
        List<Order> orders = new ArrayList<>();
        if (type == TypeRevenue.PRODUCT) {
            if (shopId != null && !shopId.equals("")) {
                orders = orderRepo.statisticProductByProductIdAndShopId(
                        new ObjectId(typeId),
                        new ObjectId(shopId));
            } else {
                orders = orderRepo.statisticProductByProductId(
                        new ObjectId(typeId));
            }
        } else if (type == TypeRevenue.CATEGORY) {
            if (shopId != null && !shopId.equals("")) {
                orders = orderRepo.statisticProductByCategoryIdAndShopId(
                        new ObjectId(typeId),
                        new ObjectId(shopId));
            } else {
                orders = orderRepo.statisticProductByCategoryId(
                        new ObjectId(typeId));
            }
        } else if (type == TypeRevenue.MANUFACTURE) {
            if (shopId != null && !shopId.equals("")) {
                orders = orderRepo.statisticProductByManufacturerIdAndShopId(
                        new ObjectId(typeId),
                        new ObjectId(shopId));
            } else {
                orders = orderRepo.statisticProductByManufacturerId(
                        new ObjectId(typeId));
            }
        }

        return caculStatisticData(orders, day, month, year, typeId, type);
    }

    private Statistic caculStatisticData(List<Order> orders, int day, int month, int year, String typeId, TypeRevenue type) {

        AtomicLong sumUtilNow = new AtomicLong();
        AtomicLong sumDay = new AtomicLong();
        AtomicLong sumMonth = new AtomicLong();
        AtomicLong sumYear = new AtomicLong();

        AtomicLong totalUtilNow = new AtomicLong();
        AtomicLong totalDay = new AtomicLong();
        AtomicLong totalMonth = new AtomicLong();
        AtomicLong totalYear = new AtomicLong();

        orders.forEach(order -> {
            var dateCreate = order.getCreateAt();
            // SUM DAY
            if (dateCreate.isAfter(LocalDate.of(year, month, day).atStartOfDay())
                    && dateCreate.isBefore(LocalDate.of(year, month, day + 1).atStartOfDay())) {
                var count = getOrderItemOfOrderProduct(order, typeId, type);
                sumDay.addAndGet(count.getQuantity());
                totalDay.addAndGet(MoneyConvert.multiBigDecimal(count.getPrice(), count.getQuantity()));
            }
            // UNTIL NOW
            if (dateCreate.isAfter(LocalDate.of(year, month, day).atStartOfDay())) {
                var count = getOrderItemOfOrderProduct(order, typeId, type);
                sumUtilNow.addAndGet(count.getQuantity());
                totalUtilNow.addAndGet(MoneyConvert.multiBigDecimal(count.getPrice(), count.getQuantity()));
            }
            // SUM MONTH
            if (dateCreate.isAfter(LocalDate.of(year, month, 1).atStartOfDay())
                    && dateCreate.isBefore(LocalDate.of(year, month + 1, 1).atStartOfDay())) {
                var count = getOrderItemOfOrderProduct(order, typeId, type);
                sumMonth.addAndGet(count.getQuantity());
                totalMonth.addAndGet(MoneyConvert.multiBigDecimal(count.getPrice(), count.getQuantity()));
            }
            // SUM YEAR
            if (dateCreate.isAfter(LocalDate.of(year, 1, 1).atStartOfDay())
                    && dateCreate.isBefore(LocalDate.of(year + 1, 1, 1).atStartOfDay())) {
                var count = getOrderItemOfOrderProduct(order, typeId, type);
                sumYear.addAndGet(count.getQuantity());
                totalYear.addAndGet(MoneyConvert.multiBigDecimal(count.getPrice(), count.getQuantity()));
            }
        });
        return Statistic.builder()
                .countUntilNow(sumUtilNow.get())
                .countYear(sumYear.get())
                .countMonth(sumMonth.get())
                .countDay(sumDay.get())
                .totalUntilNow(totalUtilNow.get())
                .totalDay(totalDay.get())
                .totalMonth(totalMonth.get())
                .totalYear(totalYear.get())
                .build();
    }

    private Order.OrderItem getOrderItemOfOrderProduct(Order order, String typeId, TypeRevenue typeRevenue) {
        if (typeRevenue == TypeRevenue.PRODUCT) {
            return order.getOrderItems().stream().filter(orderItem ->
                    orderItem.getProduct().getId().equals(typeId)
            ).findFirst().orElseThrow();
        }
        if (typeRevenue == TypeRevenue.CATEGORY) {
            return order.getOrderItems().stream().filter(orderItem ->
                    orderItem.getCategory().getId().equals(typeId)
            ).findFirst().orElseThrow();
        }
        if (typeRevenue == TypeRevenue.MANUFACTURE) {
            return order.getOrderItems().stream().filter(orderItem ->
                    orderItem.getManufacturer().getId().equals(typeId)
            ).findFirst().orElseThrow();
        }
        return null;
    }
}

package com.example.kltn.controllers.adminShop;

import com.example.kltn.constants.Constants;
import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.models.Order;
import com.example.kltn.services.payment.OrderPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adminShop")
@RequiredArgsConstructor
public class PaymentAdminController {
    private final OrderPaymentService orderPaymentService;

    @SneakyThrows
    @PutMapping("/cod/confirm/{orderId}")
    public ResponseEntity<?> confirmOrder(@PathVariable String orderId) {
        orderPaymentService.successOrder(orderId, Constants.ORDER.PAYMENT_METHOD.COD);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true, "Success", null));
    }
//    @GetMapping("/all-order")
//    private ResponseEntity<?> getAllOrder(@RequestParam int page,
//                                          @RequestParam int size) {
//        return ResponseEntity.ok(new ResponseDTO(true, "Success", orderService.findAllOrder(page, size)));
//    }
//    @GetMapping("/order/{orderId}")
//    private ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
//        return ResponseEntity.ok(new ResponseDTO(true, "Success", orderService.findByIdOrder(orderId)));
//    }

//    @GetMapping("/order/search-status")
//    public ResponseEntity<?> searchOrder(@RequestParam final String status,
//                                         @RequestParam(defaultValue = "1") final Integer page,
//                                         @RequestParam(defaultValue = "10") final Integer size) {
//        try {
//            return ResponseEntity.ok(new ResponseDTO(true, "Success",
//                    orderMapper.toOrderHistory(orderService.searchOrder(status, page, size))));
//        } catch (Exception e) {
//            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(),
//                    null));
//        }
//    }
//
//    @GetMapping("/order/count-order")
//    public ResponseEntity<?> getNumberOrderByDay(@RequestParam(defaultValue = "0") int day,
//                                                 @RequestParam(defaultValue = "0") int month,
//                                                 @RequestParam(defaultValue = "0") int year) {
//        return ResponseEntity.ok(new ResponseDTO(true, "Success",
//                orderService.countOrderByDay(day, month, year)));
//
//    }
//
//    @GetMapping("/order/count-revenue")
//    public ResponseEntity<?> getRevenueByDay(@RequestParam(defaultValue = "0") int day,
//                                             @RequestParam(defaultValue = "0") int month,
//                                             @RequestParam(defaultValue = "0") int year) {
//        return ResponseEntity.ok(new ResponseDTO(true, "Success",
//                orderService.countRevenueByDay(day, month, year)));
//
//    }
//
//    @PutMapping("/order/update-status/{id}")
//    public ResponseEntity<?> updateStatusOrders(@PathVariable Long id,
//                                                @RequestParam String status){
//        Order orderUpdate = orderService.updateStatusOrder(id, status);
//        if (orderUpdate!= null){
//            return ResponseEntity.ok(new ResponseDTO(true,"Success", orderUpdate));
//        }else return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new ResponseDTO(false,"ID order not FOUND",null));
//    }

}

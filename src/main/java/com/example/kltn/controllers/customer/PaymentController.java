package com.example.kltn.controllers.customer;

import com.example.kltn.constants.Constants;
import com.example.kltn.dto.OrderReq;
import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.services.common.OrderUserSrv;
import com.example.kltn.services.payment.OrderPaymentService;
import com.example.kltn.utils.UserUtil;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    private final OrderPaymentService orderPaymentService;
    private final OrderUserSrv orderUserSrv;

//    @Value("${redirect.order}")
//    public String redirectUrl;

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(HttpServletRequest request,
                                         @Valid @RequestBody OrderReq orderReq) throws UnsupportedEncodingException {

        String result = (String) orderPaymentService.order(orderReq, request);
        return ResponseEntity.ok(new ResponseDTO(true, "Success", result));
    }

//    @SneakyThrows
//    @Hidden
//    @GetMapping(URL_PAYPAL_CANCEL)
//    public void cancelPay(@RequestParam("token") String token, HttpServletResponse response) {
//        orderPaymentService.cancelOrder(token, PaymentConstant.PAYPAL);
//        String targerUrl = targerUrl(redirectUrl, "error", null);
//        response.sendRedirect(targerUrl);
//    }
//
//    @SneakyThrows
//    @Hidden
//    @GetMapping(URL_PAYPAL_SUCCESS)
//    public void successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletResponse response) {
//        Long orderId = (Long) orderPaymentService.successOrder(null, PaymentConstant.PAYPAL, paymentId, payerId);
//
//        String targerUrl = targerUrl(redirectUrl, "orderId", orderId.toString());
//        response.sendRedirect(targerUrl);
//    }

//    @SneakyThrows
//    @Hidden
//    @GetMapping("payment/vnpay/{orderId}")
//    public void resultVnPay(@RequestParam("vnp_ResponseCode") String responseCode,
//                            @PathVariable Long orderId,
//                            HttpServletResponse response) {
//        if (responseCode.equals("00")) {
//            orderPaymentService.successOrder(orderId, PaymentConstant.VNPAY);
//            String targerUrl = targerUrl(redirectUrl, "orderId", orderId.toString());
//            response.sendRedirect(targerUrl);
//        } else {
//            orderPaymentService.cancelOrder(orderId, PaymentConstant.VNPAY);
//            String targerUrl = targerUrl(redirectUrl, "error", orderId.toString());
//            response.sendRedirect(targerUrl);
//        }
//    }

    @PutMapping("/cod/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId) {
        orderPaymentService.cancelOrder(orderId, Constants.ORDER.PAYMENT_METHOD.COD);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(true, "Success", null));
    }

    @GetMapping("/order/history")
    public ResponseEntity<?> getOrderHistoryOfUser() {
        try {
            return ResponseEntity.ok(new ResponseDTO(true, "Success",
                    orderUserSrv.getOrderHistory(UserUtil.getIdCurrentUser())));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDTO(false, e.getMessage(),
                    null));
        }
    }
    @GetMapping("/order/{orderId}")
    private ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        return ResponseEntity.ok(new ResponseDTO(true, "Success",
                orderUserSrv.findByIdOrder(orderId)));
    }

    public String targerUrl(String redirectUrl, String param, String content) {
        return UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam(param, content)
                .build().toUriString();
    }
}

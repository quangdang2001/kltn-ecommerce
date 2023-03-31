package com.example.kltn.services.payment;


import com.example.kltn.dto.OrderReq;
import com.example.kltn.models.Order;
import com.example.kltn.services.payment.paymentFactory.PaymentProcess;
import com.example.kltn.services.payment.paymentFactory.PaymentProcessFactory;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class OrderPaymentSrvIplm implements OrderPaymentService{
    private final PaymentProcessFactory paymentProcessFactory;

    @Override
    public Object order(OrderReq orderReq, HttpServletRequest request) throws UnsupportedEncodingException {
        PaymentProcess paymentProcess = paymentProcessFactory.getPaymentMethod(orderReq.getPaymentMethod());
        Order orders = (Order) paymentProcess.initOrder(orderReq,request);
        return paymentProcess.createOrder(request,orders);
    }

    @Override
    public Object successOrder(String orderId ,String... param) throws MessagingException, TemplateException, IOException {

        PaymentProcess paymentProcess = paymentProcessFactory.getPaymentMethod(param[0]);
        String orderIdSuccess = (String) paymentProcess.confirmPaymentAndSendMail(orderId ,param);
        return orderIdSuccess;
    }

    @Override
    public void cancelOrder(Object orderId,String paymentMethod) {
        PaymentProcess paymentProcess = paymentProcessFactory.getPaymentMethod(paymentMethod);
        paymentProcess.cancelOrder(orderId);
    }
}

package com.example.kltn.services.payment;


import com.example.kltn.dto.OrderReq;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface OrderPaymentService {
    Object order(OrderReq orderReq, HttpServletRequest request) throws UnsupportedEncodingException;
    Object successOrder (String orderId, String... param) throws MessagingException, TemplateException, IOException;
    void cancelOrder(Object orderId,String paymentMethod);
}

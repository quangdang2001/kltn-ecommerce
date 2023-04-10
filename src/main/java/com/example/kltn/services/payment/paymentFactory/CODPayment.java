package com.example.kltn.services.payment.paymentFactory;


import com.example.kltn.constants.Constants;
import com.example.kltn.exceptions.NotFoundException;
import com.example.kltn.exceptions.PaymentException;
import com.example.kltn.models.Order;
import com.example.kltn.repos.*;
import com.example.kltn.services.email.EmailSenderService;
import com.example.kltn.services.payment.async.PaymentAsync;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Transactional
public class CODPayment extends PaymentProcess {


    public CODPayment(UserRepo userRepo, PaymentMethodRepo paymentMethodRepo, ModelMapper modelMapper, ProductOptionRepo productOptionRepo, ProductShopRepo productShopRepo, EmailSenderService emailSenderService, OrderRepo orderRepo, PaymentAsync paymentAsync, ShopRepo shopRepo, ProductRepo productRepo) {
        super(userRepo, paymentMethodRepo, modelMapper, productOptionRepo, productShopRepo, emailSenderService, orderRepo, paymentAsync, shopRepo, productRepo);
    }

    @Override
    public Object createOrder(HttpServletRequest request, Order orders) {
        orders.setState(Constants.ORDER.STATUS.PENDING);
        orderRepo.save(orders);
        sendEmailOrder(orders);
        return orders.getId().toString();
    }


    @Override
    public Object confirmPaymentAndSendMail(String orderId, String... param) {
        Order orders = orderRepo.findById(orderId).orElseThrow(() -> new NotFoundException("Order ID not found"));
        if (orders.getState().equals(Constants.ORDER.STATUS.CONFIRMED))
            throw new PaymentException(400, "Order has been confirmed");
        CompletableFuture<Boolean> check = paymentAsync.asyncCheckAndUpdateQuantityProduct(orders.getOrderItems()
                , orders.getOrderdetail().getShopSelected());
        orders.setState(Constants.ORDER.STATUS.CONFIRMED);

        check.exceptionally(ex -> {
            throw new PaymentException(409, "Payment failed");
        });
        orderRepo.save(orders);
        sendEmailOrder(orders);
        return orders.getId();
    }

    @Override
    public void cancelOrder(Object orderId) {
        Order orders = orderRepo.findById((String) orderId).orElseThrow(() -> new NotFoundException("Order ID not found"));
        if (orders.getState().equals(Constants.ORDER.STATUS.CANCELLED))
            throw new PaymentException(400, "Order has been canceled");
        if (orders.getState().equals(Constants.ORDER.STATUS.CONFIRMED)) {
            returnProduct(orders);
        }
        orders.setState(Constants.ORDER.STATUS.CANCELLED);
        orderRepo.save(orders);
    }
}

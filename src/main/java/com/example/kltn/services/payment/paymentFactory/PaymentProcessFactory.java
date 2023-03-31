package com.example.kltn.services.payment.paymentFactory;

import com.example.kltn.constants.Constants;
import com.example.kltn.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessFactory {
    @Autowired
    private  ApplicationContext context;

    final String message = "Not found payment method.";

    public PaymentProcess getPaymentMethod(String methodName){
        switch (methodName){
            case Constants.ORDER.PAYMENT_METHOD.PAYPAL:
//                return context.getBean(PaypalPayment.class);
            case  Constants.ORDER.PAYMENT_METHOD.VNPAY:
//                return context.getBean(VNpayPayment.class);
            case  Constants.ORDER.PAYMENT_METHOD.COD:
                return context.getBean(CODPayment.class);
            default:
                throw new NotFoundException(message);
        }
    }
}

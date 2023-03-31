package com.example.kltn.services.payment.paymentFactory;


import com.example.kltn.constants.Constants;
import com.example.kltn.dto.OrderReq;
import com.example.kltn.exceptions.NotFoundException;
import com.example.kltn.exceptions.OutOfStockException;
import com.example.kltn.models.*;
import com.example.kltn.repos.*;
import com.example.kltn.services.email.EmaiType;
import com.example.kltn.services.email.EmailSenderService;
import com.example.kltn.services.payment.async.PaymentAsync;
import com.example.kltn.utils.MoneyConvert;
import com.example.kltn.utils.UserUtil;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public abstract class PaymentProcess {
    protected final UserRepo userRepo;
    protected final PaymentMethodRepo paymentMethodRepo;
    protected final ModelMapper modelMapper;
    protected final ProductOptionRepo productOptionRepo;
    protected final ProductShopRepo productShopRepo;
    protected final EmailSenderService emailSenderService;
    protected final OrderRepo orderRepo;
    protected final PaymentAsync paymentAsync;


    protected static final String URL_PAYPAL_SUCCESS = "pay/success";
    protected static final String URL_PAYPAL_CANCEL = "pay/cancel";

    final String sold_out_product = "Sold out Product";
    final int ESTIMATE_DELIVERY_DATE = 3;

    @Transactional
    public Object initOrder(OrderReq orderReq, HttpServletRequest request) {
        String userId = UserUtil.getIdCurrentUser();

        User user = userRepo.findById(userId).orElseThrow();
        PaymentMethod payment = paymentMethodRepo.findPaymentMethodByName(orderReq.getPaymentMethod());
        if (payment == null) {
            throw new NotFoundException("Payment Method Not Found.");
        }
        LocalDateTime date = LocalDateTime.now();
        Order orders = new Order();
        orders.setState(Constants.ORDER.STATUS.PENDING);
        orders.setOrderUser(user);

        Order.OrderDetail orderDetail = modelMapper.map(orderReq, Order.OrderDetail.class);
        var orderItems = getOrderItems(orderReq);

        orderDetail.setTotalPrice(BigDecimal.valueOf(orderItems.stream().mapToInt(i -> i.getPrice().intValue()).sum()));
        orderDetail.setQuantity(orderItems.stream().mapToInt(Order.OrderItem::getQuantity).sum());

        orders.setPaymentMethod(payment);
        orders.setOrderdetail(orderDetail);
        orders.setOrderItems(orderItems);

        return orders;
    }

    @Transactional
    List<Order.OrderItem> getOrderItems(OrderReq orderReq) {
        List<Order.OrderItem> orderItems = new ArrayList<>();
        List<String> listIdPrOp = new ArrayList<>();
        orderReq.getItems().forEach(item -> {
            listIdPrOp.add(item.getProductOptionId());
        });
        List<Product.ProductShop> productShops = productShopRepo.findProductShopByProductOptions_IdAndShop_Id(listIdPrOp, orderReq.getShopSelectedId());
        if (productShops.size() == 0)
            throw new NotFoundException("Product Shop Not Found");
        var itemReqs = orderReq.getItems();
        productShops.forEach(productShop -> {
            if (productShop.getQuantity() == 0) {
                throw new NotFoundException("Out of stock Item: " + productShop.getProductOption().getId());
            }
            itemReqs.forEach(item -> {
                if (item.getProductOptionId().equals(productShop.getProductOption().getId())) {
                    item.setPrice(MoneyConvert.calculaterPrice(productShop.getProductOption().getMarketPrice(), productShop.getProductOption().getPromotion()));
                    item.setProductOption(productShop.getProductOption());
                }
            });
        });
        orderReq.getItems().forEach((item) -> {
            orderItems.add(new Order.OrderItem(item.getQuantity(), item.getPrice(), item.getProductOption()));
        });
        return orderItems;
    }

    public void returnProduct(Order order) {
        List<Order.OrderItem> orderItems = order.getOrderItems();
        Shop shop = order.getOrderdetail().getShopSelected();
        List<Product.ProductShop> productShops = productShopRepo.findProductShopByProductOption_IdInAndShop_Id(
                orderItems.stream().map(orderItem -> orderItem.getProductOption().getId()).collect(Collectors.toList()),
                shop.getId()
        );

        productShops.forEach(productShop -> {
            int quantity = orderItems.stream()
                    .filter(orderItem -> orderItem.getProductOption().getId().equals(productShop.getProductOption().getId()))
                    .findFirst().orElseThrow().getQuantity();
            productShop.setQuantity(productShop.getQuantity() + quantity);
        });
        productShopRepo.saveAll(productShops);
    }

    @SneakyThrows
    protected void sendEmailOrder(Order orders) {
        Map<String, Object> model = new HashMap<>();
        Locale locale = new Locale("vn", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        model.put("orderId", orders.getId());
        model.put("total", currencyFormatter.format(orders.getOrderdetail().getTotalPrice()));
        model.put("deliveryAddress", orders.getOrderdetail().getDeliveryAddress());
        model.put("subject", "Thank You For Your Order!");

        LocalDateTime date = orders.getCreateAt().plusDays(ESTIMATE_DELIVERY_DATE);
        String dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(date);
        model.put("deliveryDate", dateFormat);

        String title;
        if (orders.getPaymentMethod().getName().equals(Constants.ORDER.PAYMENT_METHOD.COD)) {
            if (orders.getState().equals(Constants.ORDER.STATUS.CONFIRMED))
                title = "Your order has been confirmed and will be delivery within 3 days.";
            else title = "Your order has not been confirmed and is in the process of being confirmed by Admin.";
        } else title = String.format("Your order has been paid by %s and will be delivery within 3 days.",
                orders.getPaymentMethod().getName().toUpperCase());
        model.put("title", title);

        Map<String, String> items = new HashMap<>();
        orders.getOrderItems().forEach(item -> {
            items.put(String.format("%s <b>(x%s)</br>", item.getProductOption().getFullName(), item.getQuantity()), currencyFormatter.format(item.getPrice()));
        });
        model.put("items", items);

        emailSenderService.sendEmail(orders.getOrderUser().getEmail(), model, EmaiType.ORDER);
    }

    public abstract Object createOrder(HttpServletRequest request, Order orders) throws UnsupportedEncodingException;

    public abstract Object confirmPaymentAndSendMail(String orderId,String... param) throws MessagingException, TemplateException, IOException;

    public abstract void cancelOrder(Object orderId);
}

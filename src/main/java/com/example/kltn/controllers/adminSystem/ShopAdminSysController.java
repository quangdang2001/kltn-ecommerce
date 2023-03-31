package com.example.kltn.controllers.adminSystem;

import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.models.PaymentMethod;
import com.example.kltn.models.Shop;
import com.example.kltn.repos.PaymentMethodRepo;
import com.example.kltn.repos.ShopRepo;
import com.example.kltn.utils.SlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adminSys")
@RequiredArgsConstructor
@Slf4j
public class ShopAdminSysController {
    private final ShopRepo shopRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    @PostMapping("/shop")
    ResponseEntity<?> addShop(@RequestBody Shop shop){
        Shop shopSaved = shopRepo.save(shop);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDTO(true, "Success", shopSaved)
        );
    }

    @PostMapping("/paymentMethod")
    ResponseEntity<?> addPaymentMethod(@RequestParam String paymentMethodName){
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName(paymentMethodName);
        paymentMethod.setSlug(SlugGenerator.slugify(paymentMethodName));
        paymentMethod.setIsEnable(true);

        PaymentMethod paymentMethodSaved = paymentMethodRepo.save(paymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDTO(true, "Success", paymentMethodSaved)
        );
    }

}

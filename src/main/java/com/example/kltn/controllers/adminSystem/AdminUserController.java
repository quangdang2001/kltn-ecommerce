package com.example.kltn.controllers.adminSystem;


import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.models.PaymentMethod;
import com.example.kltn.repos.PaymentMethodRepo;
import com.example.kltn.services.common.UserSrv;

import com.example.kltn.utils.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adminSys")
@RequiredArgsConstructor
//@SecurityRequirement(name = "AUTHORIZATION")
public class AdminUserController {
    private final UserSrv userService;
    private final PaymentMethodRepo paymentMethodRepo;

    @PutMapping("/users/disable/{userId}")
    public ResponseEntity<?> disable(@PathVariable String userId){
        userService.disableUserById(userId);
        return ResponseEntity.ok(new ResponseDTO(true,"Success",null));
    }
}

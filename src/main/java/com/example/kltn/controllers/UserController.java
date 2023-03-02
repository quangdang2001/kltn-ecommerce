package com.example.kltn.controllers;


import com.example.kltn.constants.TokenType;
import com.example.kltn.dto.LoginRequest;
import com.example.kltn.dto.ResponseDTO;
import com.example.kltn.models.User;
import com.example.kltn.repos.UserRepo;
import com.example.kltn.services.common.iplm.UserSrvIplm;
import com.example.kltn.services.email.EmaiType;
import com.example.kltn.services.email.EmailSenderService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserSrvIplm userService;
    private final EmailSenderService emailSenderService;

    final String TITLE_SUBJECT_EMAIL = "Shop Register TOKEN";
    final String RESET_PASSWORD_TOKEN = "Reset Password Token";

    @PostMapping("/register-email")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User userReq) throws MessagingException, TemplateException, IOException {
        log.info("Register - register for user: {}", userReq);

        String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        User.VerificationToken verificationToken = new User.VerificationToken(token, TokenType.REGISTER_TOKEN);
        log.info("Register - Token: {}", token);
        userReq.getTokens().add(verificationToken);
        User user = userService.saveRegister(userReq);

        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("title", TITLE_SUBJECT_EMAIL);
        model.put("subject", TITLE_SUBJECT_EMAIL);
        emailSenderService.sendEmail(userReq.getEmail(), model, EmaiType.REGISTER);

        return ResponseEntity.ok(new ResponseDTO(true, "Sending email",
                null));
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<?> verifyRegistration(@RequestParam("token") String token,
                                                @RequestParam("email") String email) {
        String result = userService.validateVerificationToken(token,email);
        if(!result.equals("valid")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok("");
    }

    @PostMapping("/registerTest")
    public ResponseEntity<?> registerUserTest(@RequestBody User userReq) throws MessagingException {
        User users = userService.saveAdmin(userReq);

        return ResponseEntity.ok(new ResponseDTO(true,"Success",
                null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request){

        return ResponseEntity.ok(
                new ResponseDTO(true,"Success",userService.login(loginRequest,request))
        );
    }
}

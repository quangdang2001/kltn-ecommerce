package com.example.kltn.services.common;

import com.example.kltn.dto.LoginRequest;
import com.example.kltn.models.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserSrv {
    User saveRegister(User user);
    String validateVerificationToken(String token,String email);
    User saveAdmin(User user);
    Map<String,String> login(LoginRequest loginRequest, HttpServletRequest request);
}

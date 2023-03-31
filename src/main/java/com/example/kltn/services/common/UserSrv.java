package com.example.kltn.services.common;

import com.example.kltn.constants.TokenType;
import com.example.kltn.dto.LoginRequest;
import com.example.kltn.dto.UpdateUserReq;
import com.example.kltn.models.User;

import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public interface UserSrv {
    User saveRegister(User user);

    User validateAndRegister(String token, String email);

    User saveAdmin(User user);

    Map<String, String> login(LoginRequest loginRequest, HttpServletRequest request);

    User findById(String id);

    User getCurrentUser();

    void addAddressForUser(User.Address address);

    long totalUser();

    User updateUser(UpdateUserReq userReq);

    String SendToken(String email, TokenType tokenType);

    User findUserByEmail(String email);

    User validatePasswordResetToken(String token, String email);

    void changePassword(User user, String newPassword);

    String upAvartar(MultipartFile file) throws IOException;

    void disableUserById(String usersId);

    void deleteAddressUser(String address);
}

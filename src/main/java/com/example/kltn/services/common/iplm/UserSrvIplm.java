package com.example.kltn.services.common.iplm;

import com.example.kltn.constants.Constants;
import com.example.kltn.constants.TokenType;
import com.example.kltn.dto.LoginRequest;
import com.example.kltn.dto.UpdateUserReq;
import com.example.kltn.exceptions.AppException;
import com.example.kltn.exceptions.AuthException;
import com.example.kltn.exceptions.UserException;
import com.example.kltn.models.User;
import com.example.kltn.repos.UserRepo;
import com.example.kltn.services.Cloudinary.CloudinaryUpload;
import com.example.kltn.services.auth.UserDetailIplm;
import com.example.kltn.services.common.UserSrv;
import com.example.kltn.utils.JWTProvider;
import com.example.kltn.utils.UserUtil;

import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.GenericValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSrvIplm implements UserSrv {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final CloudinaryUpload cloudinaryUpload;

    @Override
    public User saveRegister(User user) {
        if (!GenericValidator.isEmail(user.getEmail()))
            throw new UserException(400, "Wrong email");
        boolean check = userRepo.existsByEmailOrPhone(user.getEmail(), user.getPhone());
        if (check) {
            throw new UserException(400, "Email or phone already exits");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Constants.USER.ROLE.CUSTOMER);
        user.setEnable(false);
        return userRepo.save(user);
    }

    @Override
    public User validateAndRegister(String token, String email) {
        User user = userRepo.findUserByEmail(email);
        String result = validateVerificationToken(user, token, email, TokenType.REGISTER);
        if(!result.equals("valid")) {
            return null;
        }
        return user;
    }

    public String validateVerificationToken(User user, String token, String email, TokenType tokenType) {
        if (user == null) {
            return "email invalid";
        }
        User.VerificationToken tokenFound = user.getTokens().stream().filter((tokenFil) ->
                tokenFil.getToken().equals(token) && tokenFil.getTokenType().equals(tokenType)
        ).findAny().orElse(null);
        if (tokenFound == null) {
            return "invalid";
        }
        Calendar cal = Calendar.getInstance();

        if ((tokenFound.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            return "expired";
        }
        user.setEnable(true);
        user.setRole(Constants.USER.ROLE.CUSTOMER);
        userRepo.save(user);

        return "valid";
    }

    @Override
    public User saveAdmin(User user) {
        if (!GenericValidator.isEmail(user.getEmail()))
            throw new UserException(400, "Wrong email");
        boolean check = userRepo.existsByEmailOrPhone(user.getEmail(), user.getPhone());
        if (check) {
            throw new UserException(400, "Email or phone already exits");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Constants.USER.ROLE.ADMINSYS);
        user.setEnable(true);
        return userRepo.save(user);
    }

    @Override
    public Map<String, String> login(LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailIplm user = (UserDetailIplm) authentication.getPrincipal();

            String role = user.getUsers().getRole();

            String access_token = JWTProvider.createJWT(user.getUsers(), request);

            Map<String, String> token = new HashMap<>();
            token.put("access_token", access_token);
            token.put("userId", user.getUsers().getId().toString());
            token.put("email", user.getUsers().getEmail());
            token.put("name", user.getUsers().getName());
            token.put("avatar", user.getUsers().getAvatar());
            token.put("role", role);
            return token;
        } catch (AuthenticationException e) {
            throw new AuthException(403, "Access deny");
        }
    }


    @Override
    public User findById(String id) {
        return userRepo.findUserByIdExcept(id);
    }

    @Override
    public User getCurrentUser() {
        String currentUserId = UserUtil.getIdCurrentUser();
        return findById(currentUserId);
    }

    @Override
    public void addAddressForUser(User.Address address) {
        String currentUserId = UserUtil.getIdCurrentUser();
        User user = userRepo.findById(currentUserId).orElseThrow();
        if (address.getIdDefault()) {
            var addresses = user.getAddresses();
            addresses.forEach(address1 -> {
                address1.setIdDefault(false);
            });
            user.getAddresses().add(address);
        } else {
            user.getAddresses().add(address);
        }
        userRepo.save(user);
    }

    @Override
    public long totalUser() {
        return userRepo.count();
    }

    @Override
    public User updateUser(UpdateUserReq userReq) {
        User user = userRepo.findById(UserUtil.getIdCurrentUser()).orElseThrow();
        if (userReq.getGender() != null
                && List.of(Constants.USER.GENDER.MALE, Constants.USER.GENDER.FEMALE).contains(userReq.getGender())){
            user.setGender(userReq.getGender());
        }
        if (userReq.getName() != null){
            user.setName(userReq.getName());
        }
        if (userReq.getPhone() != null){
            user.setPhone(userReq.getPhone());
        }
        userRepo.save(user);
        return user;
    }

    @Override
    public String SendToken(String email, TokenType tokenType) {
        User user = userRepo.findById(UserUtil.getIdCurrentUser()).orElseThrow();
        String token = UserUtil.generateTokenUser();
        user.getTokens().add(new User.VerificationToken(token, tokenType));
        return token;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    @Override
    public User validatePasswordResetToken(String token, String email) {
        User user = userRepo.findUserByEmail(email);
        String result = validateVerificationToken(user, token, email, TokenType.RESET_PASSWORD);
        if(!result.equals("valid")) {
            return null;
        }
        return user;
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    @Override
    public String upAvartar(MultipartFile file) throws IOException {
        String id = UserUtil.getIdCurrentUser();
        User users = userRepo.findById(id).orElseThrow();
        String imgUrl = null;
        if (users.getAvatar() != null && users.getAvatar().startsWith("https://res.cloudinary.com/quangdangcloud/image/upload")) {
            imgUrl = users.getAvatar();
            imgUrl = cloudinaryUpload.uploadImage(file,imgUrl);
        }else
            imgUrl = cloudinaryUpload.uploadImage(file,null);
        users.setAvatar(imgUrl);
        userRepo.save(users);
        return imgUrl;
    }
    @Override
    public void disableUserById(String usersId) {
        User user = userRepo.findById(usersId).orElseThrow();
        user.setEnable(!user.getEnable());
        userRepo.save(user);
    }

    @Override
    public void deleteAddressUser(String address){
        User user = userRepo.findById(UserUtil.getIdCurrentUser()).orElseThrow();
        user.setAddresses(user.getAddresses().stream()
                .filter(address1 -> !address1.getFullAddress().equals(address))
                .collect(Collectors.toList()));
        userRepo.save(user);
    }
}

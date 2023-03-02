package com.example.kltn.services.common.iplm;

import com.example.kltn.constants.Constants;
import com.example.kltn.constants.TokenType;
import com.example.kltn.dto.LoginRequest;
import com.example.kltn.exceptions.AuthException;
import com.example.kltn.exceptions.UserException;
import com.example.kltn.models.User;
import com.example.kltn.repos.UserRepo;
import com.example.kltn.services.auth.UserDetailIplm;
import com.example.kltn.services.common.UserSrv;
import com.example.kltn.utils.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.GenericValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserSrvIplm implements UserSrv {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
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
    public String validateVerificationToken(String token, String email) {
        User user = userRepo.findUserByEmail(email);

        if (user == null){
            return "email invalid";
        }
        User.VerificationToken tokenFound = user.getTokens().stream().filter((tokenFil) ->
             tokenFil.getToken().equals(token) && tokenFil.getTokenType().equals(TokenType.REGISTER_TOKEN)
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
        user.setRole(Constants.USER.ROLE.ADMIN);
        user.setEnable(true);
        return userRepo.save(user);
    }

    @Override
    public Map<String, String> login(LoginRequest loginRequest, HttpServletRequest request){
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
        } catch (AuthenticationException e){
            throw new AuthException(403, "Access deny");
        }
    }
}

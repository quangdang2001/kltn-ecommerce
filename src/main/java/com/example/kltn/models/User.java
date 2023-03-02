package com.example.kltn.models;


import com.example.kltn.constants.Constants;
import com.example.kltn.constants.TokenType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Document(collection = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String email;
    private String gender;
    @Indexed(unique = true)
    private String phone;
    private String password;
    private String name;
    private String role;
    private Boolean enable = false;
    private String avatar;
    private String provider;

    private List<Address> addresses = new ArrayList<>();
    private List<VerificationToken> tokens = new ArrayList<>();


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        private String address;
        private boolean idDefault;
    }
    @Getter
    @Setter
    public static class VerificationToken {
        private static  final int EXPIRATION_TIME = 10;
        private Date expirationTime;

        private String token;
        private TokenType tokenType;

        public VerificationToken(String token, TokenType tokenType) {
            this.token = token;
            this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
            this.tokenType = tokenType;
        }

        public void setToken(String token) {
            this.token = token;
            this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
        }

        private Date calculateExpirationDate(int expirationTime) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(new Date().getTime());
            calendar.add(Calendar.MINUTE, expirationTime);
            return new Date(calendar.getTime().getTime());
        }
    }
}

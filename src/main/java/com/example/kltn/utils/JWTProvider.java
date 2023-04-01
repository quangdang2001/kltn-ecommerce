package com.example.kltn.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.kltn.models.User;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JWTProvider {

    public static String createJWT(User user, HttpServletRequest request){
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
                .withSubject(user.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000*6*24*15))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("email",user.getRole())
                .withClaim("firstName",user.getRole())
                .withClaim("lastName",user.getRole())
                .withClaim("avt",user.getRole())
                .withClaim("role",user.getRole())
                .withClaim("id",user.getRole())
                .sign(algorithm);
    }
}

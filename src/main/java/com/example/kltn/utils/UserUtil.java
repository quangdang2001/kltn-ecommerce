package com.example.kltn.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
    public static String getIdCurrentUser(){
        return String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }
    public static String generateTokenUser(){
        return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    }

}

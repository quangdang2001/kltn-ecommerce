package com.example.kltn.constants;

public enum TokenType {
    REGISTER("register"), RESET_PASSWORD("resetPassword");
    public final String value;
    TokenType(String value) {
        this.value = value;
    }

    public static TokenType getTokenType(String value) {
       return switch (value){
        case "register"
                -> TokenType.REGISTER;
        case "resetPassword"
                -> TokenType.RESET_PASSWORD;
        default
                -> null;
        };
    }
}

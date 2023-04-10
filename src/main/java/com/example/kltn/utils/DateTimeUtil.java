package com.example.kltn.utils;

public class DateTimeUtil {
    public static String generateDateQuery(int day, int month, int year){
        return String.format("ISODate('%d-%d-%d')", year,month,day);
    }
}

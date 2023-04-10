package com.example.kltn.services.statistic;

public enum TypeRevenue {
    PRODUCT("product"),
    PRODUCT_OPTION("product_option"),
    CATEGORY("category"),
    MANUFACTURE("manufacture");

    public final String value;

    TypeRevenue(String value) {
        this.value = value;
    }

    public static TypeRevenue getValue(String value) {
        return switch (value){
            case "product"
                -> TypeRevenue.PRODUCT;
            case "product_option"
                -> TypeRevenue.PRODUCT_OPTION;
            case "category"
                -> TypeRevenue.CATEGORY;
            case "manufacture"
                -> TypeRevenue.MANUFACTURE;
            default -> null;
        };
    }
}

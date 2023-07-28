package com.khouloud.ecommerceapi.common;

public enum OrderStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    PROCESSING("PROCESSING"),
    PAYMENT("PAYMENT"),
    PAID("PAID"),
    DELIVERING("DELIVERING"),
    DELIVERED("DELIVERED"),
    CANCELED("CANCELED");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return  status;
    }
}

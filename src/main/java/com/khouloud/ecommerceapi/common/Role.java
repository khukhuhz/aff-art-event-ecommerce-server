package com.khouloud.ecommerceapi.common;


public enum Role {
    UNKNOWN("UNKNOWN"),
    ADMIN("ADMIN"),
    USER("USER");

    private String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}


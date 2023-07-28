package com.khouloud.ecommerceapi.common;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class EcommerceApiResponse {
    private final Boolean success;
    private final String message;

    public String getTimestamp(){
        return LocalDateTime.now().toString();
    }
}

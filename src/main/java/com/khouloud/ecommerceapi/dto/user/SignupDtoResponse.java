package com.khouloud.ecommerceapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupDtoResponse {

    private String firstName;

    private String lastName;

    private String email;

    private Boolean status;

    private String message;
}

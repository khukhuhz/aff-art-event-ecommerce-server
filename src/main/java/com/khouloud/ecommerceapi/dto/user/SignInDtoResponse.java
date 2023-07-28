package com.khouloud.ecommerceapi.dto.user;

import com.khouloud.ecommerceapi.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SignInDtoResponse {

    private String token;

    private Role role;

    private String message;

    private Boolean status;
}

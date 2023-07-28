package com.khouloud.ecommerceapi.dto.user;

import com.khouloud.ecommerceapi.common.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SignupDtoRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 8, max = 15)
    private String password;

    @NotNull
    private Role role;
}

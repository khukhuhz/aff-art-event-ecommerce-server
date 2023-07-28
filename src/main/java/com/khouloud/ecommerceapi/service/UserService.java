package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.dto.user.SignInDtoResponse;
import com.khouloud.ecommerceapi.dto.user.SignupDtoRequest;
import com.khouloud.ecommerceapi.dto.user.SignupDtoResponse;
import com.khouloud.ecommerceapi.dto.user.SingInRequestDto;

public interface UserService {
    SignupDtoResponse signup(SignupDtoRequest signupDtoRequest);

    SignInDtoResponse signIn(SingInRequestDto singInRequestDto);

}

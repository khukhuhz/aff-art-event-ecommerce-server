package com.khouloud.ecommerceapi.controller;

import com.khouloud.ecommerceapi.common.EcommerceApiResponse;
import com.khouloud.ecommerceapi.dto.user.SignInDtoResponse;
import com.khouloud.ecommerceapi.dto.user.SignupDtoRequest;
import com.khouloud.ecommerceapi.dto.user.SignupDtoResponse;
import com.khouloud.ecommerceapi.dto.user.SingInRequestDto;
import com.khouloud.ecommerceapi.service.AuthenticationService;
import com.khouloud.ecommerceapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/e-commerce-api/v1/users")
@AllArgsConstructor
public class UserController {

    public static final String USER_IS_LOG_OUT = "L'utilisateur s'est déconnecté";
    private UserService userService;
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<SignupDtoResponse> signup(@Valid @RequestBody SignupDtoRequest signupDtoRequest){
        SignupDtoResponse signupDtoResponse = this.userService.signup(signupDtoRequest);
        return new ResponseEntity<>(signupDtoResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInDtoResponse> signIn(@Valid @RequestBody SingInRequestDto singInRequestDto){
        SignInDtoResponse signInDtoResponse = this.userService.signIn(singInRequestDto);
        return new ResponseEntity<>(signInDtoResponse, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<EcommerceApiResponse> logOut(@Valid @RequestParam("token") String token){
        Boolean isLogOut = this.authenticationService.logOut(token);
        return new ResponseEntity<>(new EcommerceApiResponse(isLogOut, USER_IS_LOG_OUT), HttpStatus.OK);
    }
}

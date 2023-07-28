package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.common.Role;
import com.khouloud.ecommerceapi.model.AuthenticationToken;
import com.khouloud.ecommerceapi.model.User;

import java.util.Optional;

public interface AuthenticationService {
    void createToken(AuthenticationToken authenticationToken);

    AuthenticationToken getToken(User user);

    User getUser(String token);

    void authenticate(String token);

    User findUserByToken(String token);

    Boolean logOut(String token);

    Optional<Boolean> isUserRole(Role role, String token);

    void verifyIfUserIsAdmin(String token);
}

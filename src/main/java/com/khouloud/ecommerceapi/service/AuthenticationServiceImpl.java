package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.common.Role;
import com.khouloud.ecommerceapi.exceptions.AuthenticationFailException;
import com.khouloud.ecommerceapi.exceptions.NotAllowedException;
import com.khouloud.ecommerceapi.model.AuthenticationToken;
import com.khouloud.ecommerceapi.model.User;
import com.khouloud.ecommerceapi.repository.AuthenticationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{

    public static final String USER_ROLE_NOT_EQUALS_TO = "Le rôle de l'utilisateur ({}) n'est pas égal à {}";
    public static final String USER_DOES_NOT_HAVE_AUTHORIZATION = "L'utilisateur n'a pas d'autorisation";
    public static final String INVALID_TOKEN = "Jeton non valide";
    public static final String TOKEN_NOT_VALID_NO_USER = "Jeton {} non valide ==> pas d'utilisateur";
    public static final String TOKEN_NOT_PRESENT = "Jeton absent";

    private final AuthenticationTokenRepository authenticationTokenRepository;

    public AuthenticationServiceImpl(AuthenticationTokenRepository authenticationTokenRepository) {
        this.authenticationTokenRepository = authenticationTokenRepository;
    }

    @Override
    public void createToken(AuthenticationToken authenticationToken) {
        this.authenticationTokenRepository.save(authenticationToken);
    }

    @Override
    public AuthenticationToken getToken(User user) {
        AuthenticationToken authenticationToken = this.authenticationTokenRepository.findByUser(user);
        return authenticationToken;
    }

    @Override
    public User getUser(String token){
        AuthenticationToken authenticationToken = authenticationTokenRepository.findByToken(token);
        if(Objects.isNull(authenticationToken)){
            log.error(TOKEN_NOT_VALID_NO_USER, token);
            throw new AuthenticationFailException(INVALID_TOKEN);
        }
        User user = authenticationToken.getUser();
        return user;
    }

    @Override
    public void authenticate(String token) {
        if(Objects.isNull(token) ){
            log.error(TOKEN_NOT_PRESENT);
            throw new AuthenticationFailException(INVALID_TOKEN);
        }
        this.getUser(token);
    }
    @Override
    public User findUserByToken(String token) {
        this.authenticate(token);
        User user = this.getUser(token);
        return user;
    }

    @Override
    public Boolean logOut(String token) {
        AuthenticationToken authenticationToken = this.authenticationTokenRepository.findByToken(token);
        if(Objects.isNull(authenticationToken)){
            log.error(TOKEN_NOT_VALID_NO_USER, token);
            throw new AuthenticationFailException(INVALID_TOKEN);
        }
        this.authenticationTokenRepository.delete(authenticationToken);
        return true;
    }

    @Override
    public Optional<Boolean> isUserRole(Role role, String token) {
        User user = this.getUser(token);
        if(role != user.getRole()){
            log.trace(USER_ROLE_NOT_EQUALS_TO, user.getRole(), role);
            return Optional.of(false);
        }
        return Optional.of(true);
    }

    @Override
    public void verifyIfUserIsAdmin(String token) {
        this.isUserRole(Role.ADMIN, token).filter(isADMIN -> isADMIN)
                .orElseThrow(() -> new NotAllowedException(USER_DOES_NOT_HAVE_AUTHORIZATION));

    }
}

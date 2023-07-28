package com.khouloud.ecommerceapi.service;

import com.khouloud.ecommerceapi.common.Role;
import com.khouloud.ecommerceapi.dto.user.SignInDtoResponse;
import com.khouloud.ecommerceapi.dto.user.SignupDtoRequest;
import com.khouloud.ecommerceapi.dto.user.SignupDtoResponse;
import com.khouloud.ecommerceapi.dto.user.SingInRequestDto;
import com.khouloud.ecommerceapi.exceptions.AuthenticationFailException;
import com.khouloud.ecommerceapi.exceptions.UserCreationException;
import com.khouloud.ecommerceapi.model.AuthenticationToken;
import com.khouloud.ecommerceapi.model.User;
import com.khouloud.ecommerceapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String USER_WITH_ADDRESS_MAIL_ALREADY_PRESENT = "Utilisateur avec adresse mail %s déjà présent";
    public static final String USER_WAS_CREATED = "L'utilisateur a été créé";
    public static final String NO_USER_WITH_LOGIN = "Aucun utilisateur avec login %s";
    public static final String WRONG_LOGIN_PASSWORD = "Mauvaise Login/mot de passe";
    public static final String SUCCESS = "succès";

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    private final ModelMapper modelMapper;

    private final String HASH_ALGORITHM = "SHA-512";

    public UserServiceImpl(UserRepository userRepository, AuthenticationService authenticationService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public SignupDtoResponse signup(SignupDtoRequest signupDtoRequest) {
        User foundUser = this.userRepository.findByEmail(signupDtoRequest.getEmail());
        if (Objects.nonNull(foundUser)) {
            throw new UserCreationException(String.format(USER_WITH_ADDRESS_MAIL_ALREADY_PRESENT, signupDtoRequest.getEmail()));
        }

        String encryptedPassword = encryptPassword(signupDtoRequest.getPassword());
        User user = this.modelMapper.map(signupDtoRequest, User.class);
        user.setPassword(encryptedPassword);
        User savedUser = userRepository.save(user);
        SignupDtoResponse signupDtoResponse = this.modelMapper.map(savedUser, SignupDtoResponse.class);
        signupDtoResponse.setStatus(true);
        signupDtoResponse.setMessage(USER_WAS_CREATED);

        final AuthenticationToken authenticationToken = new AuthenticationToken(user);
        this.authenticationService.createToken(authenticationToken);

        return signupDtoResponse;
    }

    @Override
    public SignInDtoResponse signIn(SingInRequestDto singInRequestDto) {
        User user = this.userRepository.findByEmail(singInRequestDto.getEmail());
        if (Objects.isNull(user)) {
            throw new AuthenticationFailException(String.format(NO_USER_WITH_LOGIN, singInRequestDto.getEmail()));
        }

        String encryptedPassword = encryptPassword(singInRequestDto.getPassword());
        if (!user.getEmail().equals(singInRequestDto.getEmail()) || !user.getPassword().equals(encryptedPassword)) {
            throw new AuthenticationFailException(WRONG_LOGIN_PASSWORD);
        }

        if(isRoleUserUNKNOWN(user)){
            throw new AuthenticationFailException("Unknown user role");
        }

        AuthenticationToken authenticationToken = this.authenticationService.getToken(user);
        if (isTokenNotExist(authenticationToken)) {
            authenticationToken = new AuthenticationToken(user);
            this.authenticationService.createToken(authenticationToken);
        }
        String token = authenticationToken.getToken();
        SignInDtoResponse signInDtoResponse = SignInDtoResponse.builder()//
                .token(token)//
                .role(user.getRole())//
                .message(SUCCESS)//
                .status(true)//
                .build();
        return signInDtoResponse;
    }

    private boolean isTokenNotExist(AuthenticationToken authenticationToken) {
        return Objects.isNull(authenticationToken) || authenticationToken.getToken().replaceAll("\\s", "").isEmpty();
    }

    private String encryptPassword(String password) {
        String encryptedPassword = password;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.update(password.getBytes());
            byte[] digest = messageDigest.digest();
            String hash = DatatypeConverter.printHexBinary(digest);
            encryptedPassword = hash;
        } catch (NoSuchAlgorithmException e) {
            log.info(e.getMessage());
        }
        return encryptedPassword;
    }

    public Boolean isRoleUserUNKNOWN(User user){
        return Role.UNKNOWN.equals(user.getRole());
    }
}

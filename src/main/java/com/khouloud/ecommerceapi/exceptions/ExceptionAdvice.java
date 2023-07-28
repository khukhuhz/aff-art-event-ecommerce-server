package com.khouloud.ecommerceapi.exceptions;

import com.khouloud.ecommerceapi.common.EcommerceApiResponse;
import com.khouloud.ecommerceapi.common.ValidationErrors;
import com.khouloud.ecommerceapi.dto.user.SignInDtoResponse;
import com.khouloud.ecommerceapi.dto.user.SignupDtoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<EcommerceApiResponse> handleCustomException(CustomException customException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, customException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserCreationException.class)
    public ResponseEntity<EcommerceApiResponse> handleUserCreationException(UserCreationException userCreationException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, userCreationException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AuthenticationFailException.class)
    public ResponseEntity<EcommerceApiResponse> handleAuthenticationFailException(AuthenticationFailException authenticationFailException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, authenticationFailException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotAllowedException.class)
    public ResponseEntity<EcommerceApiResponse> handleNotAllowedException(NotAllowedException notAllowedException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, notAllowedException.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NoCategoryExistsException.class)
    public ResponseEntity<EcommerceApiResponse> handleNoCategoryExistsException(NoCategoryExistsException noCategoryExistsException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, noCategoryExistsException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NoProductExistsException.class)
    public ResponseEntity<EcommerceApiResponse> handleNoProductExistsException(NoProductExistsException noProductExistsException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, noProductExistsException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ProductAlreadyExistException.class)
    public ResponseEntity<EcommerceApiResponse> handleProductAlreadyExist(ProductAlreadyExistException productAlreadyExistException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, productAlreadyExistException.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NoOrderExistsException.class)
    public ResponseEntity<EcommerceApiResponse> handleNoOrderExistsException(NoOrderExistsException noOrderExistsException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, noOrderExistsException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NoPendingOrderExistsException.class)
    public ResponseEntity<EcommerceApiResponse> handleNoPendingOrderExistsException(NoPendingOrderExistsException noPendingOrderExistsException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, noPendingOrderExistsException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PendingOrderExistsException.class)
    public ResponseEntity<EcommerceApiResponse> handlePendingOrderExistsException(PendingOrderExistsException pendingOrderExistsException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, pendingOrderExistsException.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrors> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        ValidationErrors validationErrors = new ValidationErrors(HttpStatus.BAD_REQUEST);
        validationErrors.setMessage("Validation error");
        validationErrors.addValidationErrors(exception.getBindingResult().getFieldErrors());
        validationErrors.addValidationError(exception.getBindingResult().getGlobalErrors());
        return new ResponseEntity<>(validationErrors, validationErrors.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ValidationErrors> handleConstraintViolation(ConstraintViolationException ex) {
        ValidationErrors validationErrors = new ValidationErrors(HttpStatus.BAD_REQUEST);
        validationErrors.setMessage("Validation error");
        validationErrors.addValidationErrors(ex.getConstraintViolations());
        return new ResponseEntity<>(validationErrors, validationErrors.getStatus());
    }

    @ExceptionHandler(value = ChangeStatusException.class)
    public ResponseEntity<EcommerceApiResponse> handleChangeStatusException(ChangeStatusException changeStatusException){
        return new ResponseEntity<>(new EcommerceApiResponse(false, changeStatusException.getMessage()), HttpStatus.CONFLICT);
    }
}

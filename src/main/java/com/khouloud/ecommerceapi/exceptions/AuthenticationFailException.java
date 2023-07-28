package com.khouloud.ecommerceapi.exceptions;

public class AuthenticationFailException  extends IllegalArgumentException {

    public AuthenticationFailException(String s) {
        super(s);
    }

    public AuthenticationFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationFailException(Throwable cause) {
        super(cause);
    }
}

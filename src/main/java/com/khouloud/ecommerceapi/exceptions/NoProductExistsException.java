package com.khouloud.ecommerceapi.exceptions;

public class NoProductExistsException extends RuntimeException {

    public NoProductExistsException() {
    }

    public NoProductExistsException(String message) {
        super(message);
    }

    public NoProductExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoProductExistsException(Throwable cause) {
        super(cause);
    }

    public NoProductExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

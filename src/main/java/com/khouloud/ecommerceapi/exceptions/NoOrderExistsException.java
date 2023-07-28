package com.khouloud.ecommerceapi.exceptions;

public class NoOrderExistsException extends RuntimeException {

    public NoOrderExistsException() {
    }

    public NoOrderExistsException(String message) {
        super(message);
    }

    public NoOrderExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoOrderExistsException(Throwable cause) {
        super(cause);
    }

    public NoOrderExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.khouloud.ecommerceapi.exceptions;

public class NoCategoryExistsException extends RuntimeException {

    public NoCategoryExistsException() {
    }

    public NoCategoryExistsException(String message) {
        super(message);
    }

    public NoCategoryExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCategoryExistsException(Throwable cause) {
        super(cause);
    }

    public NoCategoryExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.khouloud.ecommerceapi.exceptions;

public class NoPendingOrderExistsException extends RuntimeException {

    public NoPendingOrderExistsException() {
    }

    public NoPendingOrderExistsException(String message) {
        super(message);
    }

    public NoPendingOrderExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPendingOrderExistsException(Throwable cause) {
        super(cause);
    }

    public NoPendingOrderExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

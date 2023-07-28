package com.khouloud.ecommerceapi.exceptions;

public class PendingOrderExistsException extends RuntimeException {
    public PendingOrderExistsException() {
    }

    public PendingOrderExistsException(String message) {
        super(message);
    }

    public PendingOrderExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PendingOrderExistsException(Throwable cause) {
        super(cause);
    }

    public PendingOrderExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

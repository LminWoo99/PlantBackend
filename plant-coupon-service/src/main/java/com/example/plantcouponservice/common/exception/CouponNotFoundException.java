package com.example.plantcouponservice.common.exception;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException() {
    }

    public CouponNotFoundException(String message) {
        super(message);
    }

    public CouponNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

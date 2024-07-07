package com.hoodie.otti.exception.profile;

public class ProfileControllerException extends RuntimeException {

    public ProfileControllerException(String message) {
        super(message);
    }

    public ProfileControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}

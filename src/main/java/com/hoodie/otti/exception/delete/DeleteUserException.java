package com.hoodie.otti.exception.delete;

public class DeleteUserException extends RuntimeException {

    public DeleteUserException(String message) {
        super(message);
    }

    public DeleteUserException(String message, Throwable cause) {
        super(message, cause);
    }

    // 추가적인 예외
    public static DeleteUserException invalidToken() {
        return new DeleteUserException("Invalid token");
    }

    public static DeleteUserException userEmailMismatch() {
        return new DeleteUserException("User email does not match with token");
    }

    public static DeleteUserException userNotFound() {
        return new DeleteUserException("User not found");
    }
}

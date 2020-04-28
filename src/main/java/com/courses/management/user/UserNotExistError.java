package com.courses.management.user;

public class UserNotExistError extends RuntimeException {
    public UserNotExistError(String message) {
        super(message);
    }
}

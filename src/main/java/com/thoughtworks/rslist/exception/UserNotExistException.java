package com.thoughtworks.rslist.exception;

public class UserNotExistException extends RuntimeException {

    private String errorMessage;

    public UserNotExistException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}

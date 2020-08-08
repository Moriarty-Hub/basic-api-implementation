package com.thoughtworks.rslist.exception;

public class UnmatchedUserIdException extends RuntimeException {

    public String errorMessage;

    public UnmatchedUserIdException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}

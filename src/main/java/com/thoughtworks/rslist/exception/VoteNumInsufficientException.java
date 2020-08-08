package com.thoughtworks.rslist.exception;

public class VoteNumInsufficientException extends RuntimeException {

    private String errorMessage;

    public VoteNumInsufficientException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}

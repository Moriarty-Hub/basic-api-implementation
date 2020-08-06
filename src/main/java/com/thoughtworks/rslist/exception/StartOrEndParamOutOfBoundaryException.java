package com.thoughtworks.rslist.exception;

public class StartOrEndParamOutOfBoundaryException extends RuntimeException {

    private String errorMessage;

    public StartOrEndParamOutOfBoundaryException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}

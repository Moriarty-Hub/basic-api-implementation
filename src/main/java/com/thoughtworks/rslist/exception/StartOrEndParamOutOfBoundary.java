package com.thoughtworks.rslist.exception;

public class StartOrEndParamOutOfBoundary extends RuntimeException {

    private String errorMessage;

    public StartOrEndParamOutOfBoundary(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}

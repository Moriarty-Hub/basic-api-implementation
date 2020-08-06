package com.thoughtworks.rslist.exception;

import javax.persistence.criteria.CriteriaBuilder;

public class IndexOutOfBoundaryException extends RuntimeException {

    private String errorMessage;

    public IndexOutOfBoundaryException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}

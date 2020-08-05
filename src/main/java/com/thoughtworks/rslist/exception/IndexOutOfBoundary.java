package com.thoughtworks.rslist.exception;

import javax.persistence.criteria.CriteriaBuilder;

public class IndexOutOfBoundary extends RuntimeException {

    private String errorMessage;

    public IndexOutOfBoundary(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}

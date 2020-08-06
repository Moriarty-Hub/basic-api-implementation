package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.exception.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = UserController.class)
public class UserExceptionHandler {

    private static final String errorMessageOfInvalidArgument = "invalid user";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> definedRuleExceptionHandler() {
        Error error = new Error(errorMessageOfInvalidArgument);
        return ResponseEntity.badRequest().body(error);
    }
}

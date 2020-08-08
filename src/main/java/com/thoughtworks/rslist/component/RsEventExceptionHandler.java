package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.exception.*;
import com.thoughtworks.rslist.exception.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = RsController.class)
public class RsEventExceptionHandler {

    private static final String errorMessageOfInvalidArgument = "invalid param";

    @ExceptionHandler({StartOrEndParamOutOfBoundaryException.class, IndexOutOfBoundaryException.class})
    public ResponseEntity<Error> RsExceptionHandler(Exception exception) {
        Error error = new Error(exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> definedRuleExceptionHandler() {
        Error error = new Error(errorMessageOfInvalidArgument);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<Error> userNotExistExceptionHandler(UserNotExistException userNotExistException) {
        Error error = new Error(userNotExistException.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(UnmatchedUserIdException.class)
    public ResponseEntity<Error> unmatchedUserIdExceptionHandler(UnmatchedUserIdException unmatchedUserIdException) {
        Error error = new Error(unmatchedUserIdException.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Error> missingServletRequestParameterExceptionHandler() {
        Error error = new Error("invalid param");
        return ResponseEntity.badRequest().body(error);
    }
}

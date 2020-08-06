package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.IndexOutOfBoundaryException;
import com.thoughtworks.rslist.exception.StartOrEndParamOutOfBoundaryException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
}

package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.IndexOutOfBoundary;
import com.thoughtworks.rslist.exception.StartOrEndParamOutOfBoundary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventExceptionHandler {

    @ExceptionHandler({StartOrEndParamOutOfBoundary.class, IndexOutOfBoundary.class})
    public ResponseEntity<Error> RsExceptionHandler(Exception exception) {
        Error error = new Error(exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> definedRuleExceptionHandler(Exception exception) {
        String errorMessage = exception.getMessage();
        Error error = new Error(errorMessage
                .substring(errorMessage.lastIndexOf("default message [") + 17, errorMessage.length() - 3));
        return ResponseEntity.badRequest().body(error);
    }
}

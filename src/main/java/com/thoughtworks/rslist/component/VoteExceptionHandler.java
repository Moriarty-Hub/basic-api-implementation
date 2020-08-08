package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.api.VoteController;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.VoteNumInsufficientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = VoteController.class)
public class VoteExceptionHandler {

    @ExceptionHandler(VoteNumInsufficientException.class)
    public ResponseEntity<Error> voteNumInsufficientExceptionHandler(VoteNumInsufficientException voteNumInsufficientException) {
        Error error = new Error(voteNumInsufficientException.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

}

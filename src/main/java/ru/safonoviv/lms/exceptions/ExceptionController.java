package ru.safonoviv.lms.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = ExceptionCustomRollback.class)
    public ResponseEntity<Object> exceptionRollback(ExceptionCustomRollback e) {
        return new ResponseEntity<>(new ResponseException(e.getStatus().value(), e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> exceptionNotFound(NotFoundException e) {
        return new ResponseEntity<>(new ResponseException(e.getStatus().value(), e.getMessage()), e.getStatus());
    }
}
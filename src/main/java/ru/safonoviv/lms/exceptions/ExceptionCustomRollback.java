package ru.safonoviv.lms.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionCustomRollback extends RuntimeException{
    private final HttpStatus status;

    public ExceptionCustomRollback(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }


}

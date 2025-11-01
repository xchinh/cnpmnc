package com.example.cnpmnc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {
    private final String code;
    private final HttpStatus status;

    public BaseException(String code, String message, HttpStatus httpStatus) {
        super(message != null ? message : httpStatus.getReasonPhrase());
        this.code = code;
        this.status = httpStatus;
    }
}


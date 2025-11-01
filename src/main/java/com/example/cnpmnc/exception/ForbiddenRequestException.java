package com.example.cnpmnc.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenRequestException extends BaseException{
    public ForbiddenRequestException(String message) {
        super("10004", message, HttpStatus.FORBIDDEN);
    }
}

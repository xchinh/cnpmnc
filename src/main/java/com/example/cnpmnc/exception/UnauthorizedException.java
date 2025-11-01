package com.example.cnpmnc.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException{
    public UnauthorizedException(String message) {
        super("10001", message, HttpStatus.UNAUTHORIZED);
    }
}

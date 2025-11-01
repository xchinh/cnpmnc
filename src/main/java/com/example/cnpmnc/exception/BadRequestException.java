package com.example.cnpmnc.exception;


import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super("10000", message, HttpStatus.BAD_REQUEST);
    }
}

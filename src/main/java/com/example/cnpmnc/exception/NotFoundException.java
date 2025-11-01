package com.example.cnpmnc.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException{
    public NotFoundException(String message) {
        super("10002", message, HttpStatus.NOT_FOUND);
    }
}

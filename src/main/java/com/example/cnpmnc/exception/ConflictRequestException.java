package com.example.cnpmnc.exception;

import org.springframework.http.HttpStatus;

public class ConflictRequestException extends BaseException{
    public ConflictRequestException(String message) {
        super("10003", message, HttpStatus.CONFLICT);
    }
}

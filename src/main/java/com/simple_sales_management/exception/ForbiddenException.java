package com.simple_sales_management.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ForbiddenException extends RuntimeException{

    private String message;
    private HttpStatus status;

    public ForbiddenException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

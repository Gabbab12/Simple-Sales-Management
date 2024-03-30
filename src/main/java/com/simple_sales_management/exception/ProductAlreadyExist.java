package com.simple_sales_management.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class ProductAlreadyExist extends RuntimeException{
    private String message;
    private String status;

    public ProductAlreadyExist(String message, HttpStatus status){
        this.message = message;
        this.status = String.valueOf(status);
    }
}

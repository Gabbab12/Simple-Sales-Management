package com.simple_sales_management.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
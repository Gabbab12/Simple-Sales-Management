package com.simple_sales_management.dto;

import com.simple_sales_management.enums.Roles;
import lombok.Data;

@Data
public class SignupDto {
    private String name;
    private String lastName;
    private String password;
    private String mobile;
    private String email;
    private String address;
    private Roles roles;
}

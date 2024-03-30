package com.simple_sales_management.dto;

import lombok.Data;

@Data
public class UpdateClientDetailsDto {
    private String name;
    private String lastName;
    private String mobile;
    private String email;
    private String address;
}

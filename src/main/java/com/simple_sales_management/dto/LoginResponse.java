package com.simple_sales_management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simple_sales_management.model.User;
import lombok.Data;

@Data
public class LoginResponse {

    @JsonProperty("token")
    private String token;
    private User user;
}

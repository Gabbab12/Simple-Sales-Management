package com.simple_sales_management.service;

import com.simple_sales_management.dto.LoginRequest;
import com.simple_sales_management.dto.LoginResponse;
import com.simple_sales_management.dto.SignupDto;
import com.simple_sales_management.dto.UpdateClientDetailsDto;
import com.simple_sales_management.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<User> register(SignupDto signupDto);

    ResponseEntity<LoginResponse> login(LoginRequest request);

    ResponseEntity<User> updateClientDetails(UpdateClientDetailsDto updateClientDetailsDto);

    ResponseEntity<String> deleteClient(Long userId);

    Page<User> getAllClients(Pageable pageable);

    ResponseEntity<User> viewUser(Long userId);
}

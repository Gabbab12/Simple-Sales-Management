package com.simple_sales_management.controller;

import com.simple_sales_management.dto.LoginRequest;
import com.simple_sales_management.dto.LoginResponse;
import com.simple_sales_management.dto.SignupDto;
import com.simple_sales_management.dto.UpdateClientDetailsDto;
import com.simple_sales_management.model.User;
import com.simple_sales_management.service.implementation.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody SignupDto signupDto){
        return authService.register(signupDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @GetMapping("/get-all-clients")
    public Page<User> getAllClients(@PageableDefault(page = 0, size = 10) Pageable pageable){
        return authService.getAllClients(pageable);
    }

    @GetMapping("/view-user/{userId}")
    public ResponseEntity<User> viewUser(@PathVariable Long userId){
        return authService.viewUser(userId);
    }
    @PutMapping("/update")
    public ResponseEntity<User> updateClientDetails(@RequestBody UpdateClientDetailsDto update){
        return authService.updateClientDetails(update);
    }

    @DeleteMapping("/delete-client{userId}")
    public ResponseEntity<String> deleteClient(@PathVariable Long userId){
        return authService.deleteClient(userId);
    }
}

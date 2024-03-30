package com.simple_sales_management.service.implementation;

import com.simple_sales_management.configuration.JwtService;
import com.simple_sales_management.configuration.PasswordConfig;
import com.simple_sales_management.dto.LoginRequest;
import com.simple_sales_management.dto.LoginResponse;
import com.simple_sales_management.dto.SignupDto;
import com.simple_sales_management.dto.UpdateClientDetailsDto;
import com.simple_sales_management.enums.Roles;
import com.simple_sales_management.exception.*;
import com.simple_sales_management.model.User;
import com.simple_sales_management.repository.UserRepository;
import com.simple_sales_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordConfig passwordConfig;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Override
    public ResponseEntity<User> register(SignupDto signupDto){
        if (userRepository.existsByEmail(signupDto.getEmail())){
            throw new UserAlreadyExistException("Username already exists", HttpStatus.BAD_REQUEST);
        }
        if (!passwordConfig.validatePassword(signupDto.getPassword())){
            throw new IncorrectPasswordFormatException("incorrect Password format", HttpStatus.BAD_REQUEST);
        }

        User user = modelMapper.map(signupDto, User.class);
        user.setPassword(passwordConfig.passwordEncoder().encode(signupDto.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid email or password", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail(), HttpStatus.NOT_FOUND));

        String jwtToken = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        response.setUser(user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> updateClientDetails(UpdateClientDetailsDto updateClientDetailsDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (!user.getRoles().equals("CLIENT")){
            throw new ForbiddenException("You are not allowed to edit your details", HttpStatus.UNAUTHORIZED);
        }
        modelMapper.map(updateClientDetailsDto, user);

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
    }

    @Override
    public ResponseEntity<String> deleteClient(Long userId){
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User not found with id: " + userId, HttpStatus.NOT_FOUND)));
        userRepository.delete(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Client successfully deleted");
    }

    @Override
    public Page<User> getAllClients(Pageable pageable){
        return userRepository.findByRoles(pageable, Roles.CLIENT);
    }

    @Override
    public ResponseEntity<User> viewUser(Long userId){
        User user= userRepository.findById(userId).orElseThrow(() ->
                new ProductNotFoundException("Product with Id : " + userId + "not found", HttpStatus.NOT_FOUND));
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}

package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import com.softserve.itacademy.todolist.security.JwtService;
import com.softserve.itacademy.todolist.dto.RestAuthRequestDto;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class RestLoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RestAuthRequestDto authRequest) {
        try {
            log.info("Login attempt for user: {}", authRequest.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            User userDetails = (User) authentication.getPrincipal();

            String token = jwtService.generateToken(userDetails);
            log.info("Login successful for user: {}, JWT token generated.", authRequest.getUsername());

            return ResponseEntity.ok(token);  // Return JWT token
        } catch (AuthenticationException e) {
            log.error("Login failed for user: {}. Error: {}", authRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            log.error("Unexpected error during login for user: {}. Error: {}", authRequest.getUsername(), e.getMessage());
            throw new AuthenticationServiceException("Unexpected error during login", e);
        }
    }
}

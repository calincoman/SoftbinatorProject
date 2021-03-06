package com.example.softbinatorproject.controllers;

import com.example.softbinatorproject.dtos.LoginDto;
import com.example.softbinatorproject.dtos.RefreshTokenDto;
import com.example.softbinatorproject.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login-user")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(authService.login(loginDto, "USER"), HttpStatus.OK);
    }

    @PostMapping("/login-store")
    public ResponseEntity<?> loginStore(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(authService.login(loginDto, "STORE"), HttpStatus.OK);
    }

    @PostMapping("/login-admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(authService.login(loginDto, "ADMIN"), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(RefreshTokenDto refreshTokenDto) {
        return new ResponseEntity<>(authService.refresh(refreshTokenDto), HttpStatus.OK);
    }

}


package com.example.softbinatorproject.controllers;


import com.example.softbinatorproject.dtos.RegisterUserDto;
import com.example.softbinatorproject.services.UserService;
import com.example.softbinatorproject.utils.KeycloakHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getDetails(Authentication authentication) {
        return new ResponseEntity<>(KeycloakHelper.getUser(authentication), HttpStatus.OK);
    }

    @GetMapping("/logged-in-user-info")
    public ResponseEntity<?> getLoggedInUser(Authentication authentication) {
        return new ResponseEntity<>(KeycloakHelper.getLoggedInUser(authentication), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        return new ResponseEntity<>(userService.registerUser(registerUserDto), HttpStatus.OK);
    }
}


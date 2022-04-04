package com.example.softbinatorproject.controllers;


import com.example.softbinatorproject.dtos.RegisterStoreDto;
import com.example.softbinatorproject.dtos.RegisterUserDto;
import com.example.softbinatorproject.models.Product;
import com.example.softbinatorproject.repositories.UserRepository;
import com.example.softbinatorproject.services.ProductService;
import com.example.softbinatorproject.services.UserService;
import com.example.softbinatorproject.utils.KeycloakUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ProductService productService;

    public UserController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getLoggedUser(Authentication authentication) {
        return new ResponseEntity<>(userService.getUserFromKeycloakUserId(KeycloakUtils.getUser(authentication)),
                HttpStatus.OK);
    }

    @GetMapping("/info-all-users")
    public ResponseEntity<?> getAllUserDtos(Authentication authentication) {
        return new ResponseEntity<>(userService.getUserDtos(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getUsersInfo() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        return new ResponseEntity<>(userService.registerUser(registerUserDto, "ROLE_USER"), HttpStatus.OK);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterUserDto registerUserDto) {
        return new ResponseEntity<>(userService.registerUser(registerUserDto, "ROLE_ADMIN"), HttpStatus.OK);
    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, Authentication authentication) {
        product.setUserId(Long.parseLong(KeycloakUtils.getUser(authentication)));
        return new ResponseEntity<>(productService.addProduct(product), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }
}


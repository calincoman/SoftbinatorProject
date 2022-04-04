package com.example.softbinatorproject.services;


import com.example.softbinatorproject.dtos.RegisterUserDto;
import com.example.softbinatorproject.dtos.TokenDto;
import com.example.softbinatorproject.dtos.UserInfoDto;
import com.example.softbinatorproject.models.Product;
import com.example.softbinatorproject.models.User;
import com.example.softbinatorproject.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final ProductService productService;

    @Autowired
    public UserService(UserRepository userRepository, KeycloakAdminService keycloakAdminService,
                       ProductService productService) {
        this.userRepository = userRepository;
        this.keycloakAdminService = keycloakAdminService;
        this.productService = productService;
    }

    public User getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        return user;
    }

    public UserInfoDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        return UserInfoDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @SneakyThrows
    public TokenDto registerUser(RegisterUserDto registerUserDto, String role) {

        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new BadRequestException("User with email " + registerUserDto.getEmail() + " already exists");
        }

        if (userRepository.existsByUsername(registerUserDto.getUsername())) {
            throw new BadRequestException("User with username " + registerUserDto.getUsername() + " already exists");
        }

        User newUser = User.builder()
                .username(registerUserDto.getUsername())
                .email(registerUserDto.getEmail())
                .identityCard(registerUserDto.getIdentityCard())
                .build();
        Long userId = userRepository.save(newUser).getId();
        TokenDto token = keycloakAdminService.addUserToKeycloak(userId, registerUserDto.getPassword(), role);

        return token;
    }

    public User deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        productService.deleteProductsOfUser(userId);
        userRepository.deleteById(userId);
        return user;
    }

    public UserInfoDto getUserFromKeycloakUserId(String keycloakUserId) {
        User user = userRepository.findById(Long.parseLong(keycloakUserId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        return UserInfoDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public List<UserInfoDto> getUserDtos() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> UserInfoDto.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .email(user.getEmail())
                                .build())
                .collect(Collectors.toList());
    }

    public List<Product> getProductsOfUser(Long userId) {
        User user = this.getUser(userId);

        if (user.getOfferedProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not have any products");
        }

        return user.getOfferedProducts();
    }
}

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

@Service
public class UserService {
    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;

    @Autowired
    public UserService(UserRepository userRepository, KeycloakAdminService keycloakAdminService) {
        this.userRepository = userRepository;
        this.keycloakAdminService = keycloakAdminService;
    }

    public UserInfoDto getUser(Long userId) {

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
    public TokenDto registerUser(RegisterUserDto registerUserDto) {

        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new BadRequestException("User with email " + registerUserDto.getEmail() + " already exists");
        }

        if (userRepository.existsByUsername(registerUserDto.getUsername())) {
            throw new BadRequestException("User with username " + registerUserDto.getUsername() + " already exists");
        }

        User newUser = User.builder()
                .username(registerUserDto.getUsername())
                .email(registerUserDto.getEmail())
                .build();
        Long userId = userRepository.save(newUser).getId();
        TokenDto token = keycloakAdminService.addUserToKeycloak(userId, registerUserDto.getPassword());

        return token;
    }

    public User deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exist"));
        userRepository.deleteById(id);
        return user;
    }
}

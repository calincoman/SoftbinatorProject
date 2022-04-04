package com.example.softbinatorproject.services;

import com.example.softbinatorproject.clients.AuthClient;
import com.example.softbinatorproject.dtos.LoginDto;
import com.example.softbinatorproject.dtos.RefreshTokenDto;
import com.example.softbinatorproject.dtos.TokenDto;
import com.example.softbinatorproject.models.User;
import com.example.softbinatorproject.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthClient authClient;
    private final UserRepository userRepository;

    @Value("${keycloak.resource}")
    private String keycloakClient;

    @Autowired
    public AuthService(AuthClient authClient, UserRepository userRepository) {
        this.authClient = authClient;
        this.userRepository = userRepository;
    }

    @SneakyThrows
    public TokenDto login(LoginDto loginDto) {

        User databaseUser = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        MultiValueMap<String, String> loginCredentials = new LinkedMultiValueMap<>();
        loginCredentials.add("client_id", keycloakClient);
        loginCredentials.add("username", databaseUser.getId().toString());
        loginCredentials.add("password", loginDto.getPassword());
        loginCredentials.add("grant_type", loginDto.getGrantType());

        TokenDto token = authClient.login(loginCredentials);
        return token;

    }

    @SneakyThrows
    public TokenDto refresh(RefreshTokenDto refreshTokenDto) {

        MultiValueMap<String, String> refreshCredentials = new LinkedMultiValueMap<>();
        refreshCredentials.add("client_id", keycloakClient);
        refreshCredentials.add("refresh_token", refreshTokenDto.getRefreshToken());
        refreshCredentials.add("grant_type", refreshTokenDto.getGrantType());

        TokenDto token = authClient.refresh(refreshCredentials);
        return token;
    }

}

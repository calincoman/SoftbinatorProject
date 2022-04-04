package com.example.softbinatorproject.services;

import com.example.softbinatorproject.clients.AuthClient;
import com.example.softbinatorproject.dtos.LoginDto;
import com.example.softbinatorproject.dtos.RefreshTokenDto;
import com.example.softbinatorproject.dtos.TokenDto;
import com.example.softbinatorproject.models.Store;
import com.example.softbinatorproject.models.User;
import com.example.softbinatorproject.repositories.StoreRepository;
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
    private final StoreRepository storeRepository;

    @Value("${keycloak.resource}")
    private String keycloakClient;

    @Autowired
    public AuthService(AuthClient authClient, UserRepository userRepository, StoreRepository storeRepository) {
        this.authClient = authClient;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }


    @SneakyThrows
    public TokenDto login(LoginDto loginDto, String role) {

        String keycloakUsername = "";
        if (role.equals("USER") || role.equals("ADMIN")) {
            User databaseUser = userRepository.findByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
            keycloakUsername = databaseUser.getId().toString();
        } else if (role.equals("STORE")) {
            Store databaseStore = storeRepository.findByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store does not exist"));
            keycloakUsername = databaseStore.getId().toString();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }

        MultiValueMap<String, String> loginCredentials = new LinkedMultiValueMap<>();
        loginCredentials.add("client_id", keycloakClient);
        loginCredentials.add("username", keycloakUsername);
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

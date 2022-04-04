package com.example.softbinatorproject.utils;

import com.example.softbinatorproject.models.User;
import com.example.softbinatorproject.repositories.UserRepository;
import lombok.NoArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;


public class KeycloakUtils {

    public static String getUser(Authentication authentication) {
        String keycloakUserId = ((KeycloakPrincipal) authentication.getPrincipal()).getKeycloakSecurityContext()
                .getToken().getPreferredUsername();

        return keycloakUserId;
    }

    public static String getLoggedInUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal principal = (KeycloakPrincipal) authentication.getPrincipal();
            return principal.getName();
        } else {
            return null;
        }
    }
}


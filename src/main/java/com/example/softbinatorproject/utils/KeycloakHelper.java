package com.example.softbinatorproject.utils;

import com.example.softbinatorproject.dtos.UserInfoDto;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.Authentication;

public class KeycloakHelper {

    public static String getUser(Authentication authentication) {
        return ((KeycloakPrincipal) authentication.getPrincipal()).getKeycloakSecurityContext()
                .getToken().getPreferredUsername();
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


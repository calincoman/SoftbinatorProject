package com.example.softbinatorproject.services;

import com.example.softbinatorproject.clients.AuthClient;
import com.example.softbinatorproject.dtos.ChangePasswordDto;
import com.example.softbinatorproject.dtos.TokenDto;
import com.example.softbinatorproject.models.User;
import com.example.softbinatorproject.repositories.UserRepository;
import lombok.Data;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.resource}")
    private String keycloakClient;

    private final Keycloak keycloak;
    private final AuthClient authClient;
    private final UserRepository userRepository;
    private RealmResource realm;

    @Autowired
    public KeycloakAdminService(Keycloak keycloak, AuthClient authClient,
                                UserRepository userRepository) {
        this.keycloak = keycloak;
        this.authClient = authClient;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initRealmResource() {
        this.realm = this.keycloak.realm(keycloakRealm);
    }

    public TokenDto addUserToKeycloak(Long userId, String password) {

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setEnabled(true);
        keycloakUser.setUsername(userId.toString());

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();

        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        credentialRepresentation.setTemporary(false);

        keycloakUser.setCredentials(Collections.singletonList(credentialRepresentation));

        Response response = realm.users().create(keycloakUser);
        String keycloakUserId = getCreatedId(response);
        System.out.println("User has been saved with an id: " + keycloakUserId);

        UserResource userResource = realm.users().get(keycloakUserId);
        RoleRepresentation roleRepresentation = realm.roles().get("ROLE_USER").toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));

        MultiValueMap<String, String> loginCredentials = new LinkedMultiValueMap<>();
        loginCredentials.add("client_id", keycloakClient);
        loginCredentials.add("username", userId.toString());
        loginCredentials.add("password", password);
        loginCredentials.add("grant_type", "password");

        TokenDto token = authClient.login(loginCredentials);

        return token;

    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        UserRepresentation userRepresentation = realm.users().search(id.toString()).get(0);

        realm.users().delete(userRepresentation.getId());
    }

    public void changePassword(ChangePasswordDto changePasswordDto) {

        User user = userRepository.findByEmail(changePasswordDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        UserRepresentation userRepresentation = realm.users().search(user.getId().toString()).get(0);

        CredentialRepresentation newCredential = new CredentialRepresentation();
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(changePasswordDto.getNewPassword());
        newCredential.setTemporary(false);
        userRepresentation.setCredentials(Collections.singletonList(newCredential));

        realm.users().get(userRepresentation.getId()).update(userRepresentation);
    }

}


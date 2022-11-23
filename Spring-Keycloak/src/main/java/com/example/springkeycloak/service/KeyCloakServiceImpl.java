package com.example.springkeycloak.service;

import com.example.springkeycloak.configuration.KeycloakConfiguration;
import com.example.springkeycloak.dto.request.KeycloakCreateUserRequest;
import com.example.springkeycloak.dto.request.KeycloakResetPasswordRequest;
import com.example.springkeycloak.dto.request.KeycloakUpdateUserRequest;
import com.example.springkeycloak.dto.response.KeycloakRegisterUserResponse;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.springkeycloak.configuration.KeycloakConfiguration.createPasswordCredentials;

@Service
public class KeyCloakServiceImpl implements KeyCloakService {
    private final KeycloakConfiguration keycloakConfiguration;

    public KeyCloakServiceImpl(KeycloakConfiguration keycloakConfiguration) {
        this.keycloakConfiguration = keycloakConfiguration;
    }

    @Override
    public KeycloakRegisterUserResponse createUser(KeycloakCreateUserRequest userDTO) {
        UserRepresentation user = new UserRepresentation();
        CredentialRepresentation credential = createPasswordCredentials(userDTO.getPassword());
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        user.setRealmRoles(List.of("user", "admin"));
        rolesToRealmRoleRepresentation(List.of("user","admin"));
        Response response = getInstance("example").create(user);
        return new KeycloakRegisterUserResponse(getCreatedId(response));

    }

    @Override
    public List<UserRepresentation> listUser() {
        return getInstance("example").list();
    }

    @Override
    public void updateUser(String userId, KeycloakUpdateUserRequest user) {
        UserRepresentation userInfo = new UserRepresentation();
        userInfo.setFirstName(user.getFirstname());
        userInfo.setLastName(user.getLastName());
        userInfo.setEmail(user.getEmailId());
        userInfo.setEnabled(true);
        getInstance("example").get(userId).update(userInfo);
    }

    @Override
    public void deleteUser(String userId) {
        getInstance("example").get(userId).remove();
    }

    @Override
    public void sendVerificationLink(String userId) {
        getInstance("example").get(userId).sendVerifyEmail();
    }

    @Override
    public void resetPassword(String userId, KeycloakResetPasswordRequest keycloakResetPasswordRequest) {
        CredentialRepresentation credential = createPasswordCredentials(keycloakResetPasswordRequest.getPassword());
        getInstance("example").get(userId).resetPassword(credential);
    }

    public UsersResource getInstance(String realm) {
        return keycloakConfiguration.keycloakInstance().realm(realm).users();
    }


    public void addRealmRoleToUser(String userName) {
        List<RoleRepresentation> list = new ArrayList<>();
        keycloakConfiguration.keycloakInstance().realm("example").users().get(userName).roles().realmLevel().add(list);

    }

    private String getCreatedId(Response response) {
        URI location = response.getLocation();
        if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
            Response.StatusType statusInfo = response.getStatusInfo();
            if (statusInfo.getStatusCode() == 409) {
                throw new RuntimeException("Status Code " + statusInfo.getStatusCode() + " Username or Email already exists");
            }
            if (statusInfo.getStatusCode() == 403) {
                throw new RuntimeException("Status Code " + statusInfo.getStatusCode() + " " + statusInfo.getReasonPhrase());
            }
            if (statusInfo.getStatusCode() == 404) {
                throw new RuntimeException("Status Code " + statusInfo.getStatusCode() + " " + statusInfo.getReasonPhrase());
            }
        }
        if (location == null) return "EMPTY";
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private List<RoleRepresentation> rolesToRealmRoleRepresentation(List<String> roles) {
        List<RoleRepresentation> existingRoles = keycloakConfiguration.keycloakInstance().realm("example").roles().list();

        List<String> serverRoles = existingRoles.stream().map(RoleRepresentation::getName).collect(Collectors.toList());
        List<RoleRepresentation> resultRoles = new ArrayList<>();

        for (String role : roles) {
            int index = serverRoles.indexOf(role);
            if (index != -1) {
                resultRoles.add(existingRoles.get(index));
            } else {
                System.out.println("Role doesn't exist");
            }
        }
        return resultRoles;
    }
}

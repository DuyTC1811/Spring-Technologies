package com.example.springkeycloak.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

@Component
public class KeycloakConfiguration {
    @Value("${keycloak-initializer.realm}")
    private String realm;
    @Value("${keycloak-initializer.client-id}")
    private String clientId;
    @Value("${keycloak-initializer.username}")
    private String username;
    @Value("${keycloak-initializer.password}")
    private String password;
    @Value("${keycloak-initializer.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak-initializer.credentials-secret}")
    private String clientSecret;

    @Bean
    public Keycloak keycloakInstance() {
        return KeycloakBuilder.builder()
                .grantType(CLIENT_CREDENTIALS)
                .serverUrl(authServerUrl)
                .username(username)
                .password(password)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    public static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}

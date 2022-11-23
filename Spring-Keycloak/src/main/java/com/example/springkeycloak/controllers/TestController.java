package com.example.springkeycloak.controllers;

////import com.example.springkeycloak.configuration.KeycloakConfiguration;
//import org.keycloak.admin.client.resource.RealmResource;
//import org.keycloak.representations.AccessTokenResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//
//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
//    private final KeycloakConfiguration keycloak;
//
//    public TestController(KeycloakConfiguration keycloak) {
//        this.keycloak = keycloak;
//    }


    @GetMapping("/all")
    public String allAccess() {
//        AccessTokenResponse a = keycloak.keycloakConfig().tokenManager().getAccessToken();
//        System.out.println(a);
        return "Public Content.";
    }

    @GetMapping("/user")
//    @PreAuthorize("hasRole('USER')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
//    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}

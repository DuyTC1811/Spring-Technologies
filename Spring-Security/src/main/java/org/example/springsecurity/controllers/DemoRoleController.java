package org.example.springsecurity.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo/roles")
@Tag(name = "DEMO ROLES", description = "API DEMO ROLE")
public class DemoRoleController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminApi() {
        return "Only ADMIN can access this API";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userApi() {
        return "Only USER can access this API";
    }

    @GetMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public String staffApi() {
        return "Only STAFF can access this API";
    }

    @GetMapping("/technique")
    @PreAuthorize("hasRole('TECHNIQUE')")
    public String techniqueApi() {
        return "Only TECHNIQUE can access this API";
    }

    @GetMapping("/admin-or-staff")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public String adminOrStaffApi() {
        return "ADMIN or STAFF can access this API";
    }
}
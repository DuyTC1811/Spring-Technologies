package org.example.springsecurity.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo/permissions")
@Tag(name = "DEMO PERMISSIONS", description = "API DEMO PERMISSION")
public class DemoPermissionController {

    @GetMapping("/view")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public String viewApi() {
        return "Require permission USER_VIEW";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public String createApi() {
        return "Require permission USER_CREATE";
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public String editApi() {
        return "Require permission USER_EDIT";
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public String deleteApi() {
        return "Require permission USER_DELETE";
    }

    @GetMapping("/view-or-edit")
    @PreAuthorize("hasAnyAuthority('USER_VIEW', 'USER_EDIT')")
    public String viewOrEditApi() {
        return "Require permission USER_VIEW or USER_EDIT";
    }
}

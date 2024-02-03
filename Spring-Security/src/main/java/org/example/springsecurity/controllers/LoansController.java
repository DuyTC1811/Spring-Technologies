package org.example.springsecurity.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class LoansController {
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/loan")
    public String getLoan() {
        return "This Loan Controller";
    }

    @GetMapping("/password")
    public String getPassword() {
        UserDetails userDetails = User.withUsername("DuyTC")
                .username("DuyTC")
                .password(passwordEncoder.encode("duytc"))
                .roles("USER", "ADMIN")
                .build();
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(userDetails);
        return "User " + userDetails.getPassword() + "\nPass " + userDetails.getPassword() + "\nRole " + userDetails.getAuthorities();
    }
}

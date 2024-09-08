package com.example.springsso.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class UserInfo implements OAuth2User, UserDetails {
    private String userId;
    private String username;
    private String password;
    private String status;
    private String email;
    private String mobile;
    private Set<GrantedAuthority> authorities;
    @Override
    public String getPassword() {
        return "asdf";
    }

    @Override
    public String getUsername() {
        return "duytc";
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo user = (UserInfo) o;
        return Objects.equals(userId, user.userId);
    }
}

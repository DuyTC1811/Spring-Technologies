package org.example.springsecurity.configurations.security;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NullMarked
public class UserInfo implements UserDetails {
    private String userId;
    private String username;
    private String password;
    private String status;
    private int tokenVersion;
    private Set<GrantedAuthority> authorities = new HashSet<>();

    public UserInfo() {
    }

    public void setRoleCodes(Set<String> roleCodes) {
        if (CollectionUtils.isEmpty(roleCodes)) {
            return;
        }

        this.authorities.addAll(roleCodes.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet()));
    }

    public void setPermissionCodes(Set<String> permissionCodes) {
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return;
        }

        this.authorities.addAll(permissionCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet()));
    }

    public boolean hasAuthority(String authority) {
        return this.authorities.stream()
                .anyMatch(item -> Objects.equals(item.getAuthority(), authority));
    }

    public boolean hasRole(String roleCode) {
        return hasAuthority("ROLE_" + roleCode);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo user)) return false;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}

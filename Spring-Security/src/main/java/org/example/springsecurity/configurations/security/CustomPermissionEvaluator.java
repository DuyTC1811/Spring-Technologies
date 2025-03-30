package org.example.springsecurity.configurations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
//    private final IPermissionHandler permissionHandler;
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        System.out.println(authentication);
        System.out.println(targetDomainObject);
        System.out.println(permission);
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        System.out.println(authentication);
        System.out.println(targetId);
        System.out.println(targetType);
        System.out.println(permission);
        return false;
    }
}

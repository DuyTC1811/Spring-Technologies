package org.example.springsecurity.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecurity.configurations.security.UserInfo;
import org.example.springsecurity.models.PolicyRule;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("authz")
@RequiredArgsConstructor
public class AuthzService {
    private final PolicyEvaluator policyEvaluator;
//    private final PolicyMapper policyMapper;
//    private final UserMapper userMapper;

    public boolean canAccess(Authentication authentication, String permissionCode, String resourceId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserInfo user)) {
            return false;
        }
        boolean allowed = user.hasAuthority(permissionCode);

        if (!allowed) {
            log.warn("ACCESS DENIED userId={}, username={}, permissionCode={}, resourceId={}",
                    user.getUserId(), user.getUsername(), permissionCode, resourceId);
        }


        UserEntity resource = userMapper.findById(resourceId);
        if (resource == null) {
            return false;
        }

        List<PolicyRule> rules = policyMapper.findRulesByPermissionCode(permissionCode);
//
        return policyEvaluator.evaluate(user, resource, rules);
//        return allowed;
        return true;
    }
}

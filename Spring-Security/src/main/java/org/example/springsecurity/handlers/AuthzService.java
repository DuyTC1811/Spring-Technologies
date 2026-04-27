package org.example.springsecurity.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecurity.configurations.security.UserInfo;
import org.example.springsecurity.models.PolicyRule;
import org.example.springsecurity.mappers.IPolicyMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Combo RBAC + ABAC. Caller dùng trong @PreAuthorize:
 *   @PreAuthorize("@authz.canAccess(authentication, 'PERM_X', #resource)")
 * RBAC quyết định trước (user có authority/role không); nếu pass mới đánh giá policy
 * rule (ABAC) gắn với permission.
 */
@Slf4j
@Component("authz")
@RequiredArgsConstructor
public class AuthzService {
    private final PolicyEvaluator policyEvaluator;
    private final IPolicyMapper policyMapper;

    public boolean canAccess(Authentication authentication, String permissionCode, Object resource) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserInfo user)) {
            return false;
        }

        if (!user.hasAuthority(permissionCode)) {
            log.warn("ACCESS DENIED rbac userId={} permissionCode={}", user.getUserId(), permissionCode);
            return false;
        }

        List<PolicyRule> rules = policyMapper.findRulesByPermissionCode(permissionCode);
        boolean ok = policyEvaluator.evaluate(user, resource, rules);
        if (!ok) {
            log.warn("ACCESS DENIED abac userId={} permissionCode={}", user.getUserId(), permissionCode);
        }
        return ok;
    }

    public boolean canAccess(Authentication authentication, String permissionCode) {
        return canAccess(authentication, permissionCode, null);
    }
}

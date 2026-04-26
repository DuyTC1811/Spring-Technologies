package org.example.springsecurity.handlers;

import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.example.springsecurity.configurations.security.UserInfo;
import org.example.springsecurity.models.PolicyRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PolicyEvaluator {
    public boolean evaluate(UserInfo user, Object resource, List<PolicyRule> rules) {
        for (PolicyRule rule : rules) {
            boolean matched = evaluateRule(user, resource, rule);

            if ("DENY".equalsIgnoreCase(rule.getEffect()) && matched) {
                return false;
            }

            if ("ALLOW".equalsIgnoreCase(rule.getEffect()) && !matched) {
                return false;
            }
        }

        return true;
    }

    private boolean evaluateRule(UserPrincipal user, Object resource, PolicyRule rule) {
        Object leftValue = getValue(user, rule.getSubjectAttribute());
        Object rightValue;

        if (rule.getExpectedValue() != null) {
            rightValue = rule.getExpectedValue();
        } else {
            rightValue = getValue(resource, rule.getResourceAttribute());
        }

        return switch (rule.getOperator()) {
            case "EQ" -> Objects.equals(String.valueOf(leftValue), String.valueOf(rightValue));
            case "NE" -> !Objects.equals(String.valueOf(leftValue), String.valueOf(rightValue));
            default -> throw new IllegalArgumentException("Unsupported operator: " + rule.getOperator());
        };
    }

    private Object getValue(Object target, String fieldName) {
        if (target == null || fieldName == null) {
            return null;
        }

        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot read field: " + fieldName, e);
        }
    }
}

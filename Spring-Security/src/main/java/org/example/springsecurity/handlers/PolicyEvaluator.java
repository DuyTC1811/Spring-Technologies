package org.example.springsecurity.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.example.springsecurity.configurations.security.UserInfo;
import org.example.springsecurity.models.PolicyRule;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PolicyEvaluator {
    private static final String EFFECT_DENY = "DENY";
    private static final String EFFECT_ALLOW = "ALLOW";

    /**
     * ABAC evaluation rules:
     * <p>
     * 1. No policy rules -> allow because RBAC already passed.
     * 2. DENY policy matched -> deny immediately.
     * 3. ALLOW policy exists -> at least one ALLOW policy must match.
     * 4. One policy is matched -> all rules in that policy must match.
     */
    public boolean evaluate(UserInfo user, Object resource, List<PolicyRule> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return true;
        }

        Map<String, List<PolicyRule>> rulesByPolicy = groupByPolicy(rules);

        boolean hasAllowPolicy = false;
        boolean hasMatchedAllowPolicy = false;

        for (List<PolicyRule> policyRules : rulesByPolicy.values()) {
            if (CollectionUtils.isEmpty(rules)) {
                continue;
            }

            String effect = getPolicyEffect(policyRules);

            boolean policyMatched = isPolicyMatched(user, resource, policyRules);

            if (isDeny(effect) && policyMatched) {
                return false;
            }

            if (isAllow(effect)) {
                hasAllowPolicy = true;

                if (policyMatched) {
                    hasMatchedAllowPolicy = true;
                }
            }
        }

        return !hasAllowPolicy || hasMatchedAllowPolicy;
    }

    private Map<String, List<PolicyRule>> groupByPolicy(List<PolicyRule> rules) {
        return rules.stream()
                .collect(Collectors.groupingBy(PolicyRule::getPolicyId));
    }

    private String getPolicyEffect(List<PolicyRule> policyRules) {
        return policyRules.getFirst().getEffect();
    }

    private boolean isPolicyMatched(UserInfo user, Object resource, List<PolicyRule> policyRules) {
        return policyRules.stream()
                .allMatch(rule -> isRuleMatched(user, resource, rule));
    }

    private boolean isRuleMatched(UserInfo user, Object resource, PolicyRule rule) {
        Object actualValue = readSubjectValue(user, rule);
        Object expectedValue = resolveExpectedValue(resource, rule);

        String operator = normalizeOperator(rule.getOperator());

        return switch (operator) {
            case "EQ" -> isEqual(actualValue, expectedValue);
            case "NE" -> !isEqual(actualValue, expectedValue);
            case "GT" -> compareNumber(actualValue, expectedValue) > 0;
            case "GTE" -> compareNumber(actualValue, expectedValue) >= 0;
            case "LT" -> compareNumber(actualValue, expectedValue) < 0;
            case "LTE" -> compareNumber(actualValue, expectedValue) <= 0;
            case "IN" -> isInList(actualValue, expectedValue);
            case "NOT_IN" -> !isInList(actualValue, expectedValue);
            case "CONTAINS" -> contains(actualValue, expectedValue);
            default -> throw new IllegalArgumentException("Unsupported policy operator: " + operator);
        };
    }

    private Object readSubjectValue(UserInfo user, PolicyRule rule) {
        return getFieldValue(user, rule.getSubjectAttribute());
    }

    private Object resolveExpectedValue(Object resource, PolicyRule rule) {
        if (rule.getExpectedValue() != null) {
            return rule.getExpectedValue();
        }

        return getFieldValue(resource, rule.getResourceAttribute());
    }

    private Object getFieldValue(Object target, String fieldName) {
        if (target == null || isBlank(fieldName)) {
            return null;
        }

        if (target instanceof Map<?, ?> map) {
            return map.get(fieldName);
        }

        return getObjectFieldValue(target, fieldName);
    }

    private Object getObjectFieldValue(Object target, String fieldName) {
        Class<?> clazz = target.getClass();

        while (clazz != null && clazz != Object.class) {
            Field field = findField(clazz, fieldName);

            if (field != null) {
                return readFieldValue(target, field);
            }

            clazz = clazz.getSuperclass();
        }

        return null;
    }

    private Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException exception) {
            return null;
        }
    }

    private Object readFieldValue(Object target, Field field) {
        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (IllegalAccessException exception) {
            throw new IllegalArgumentException("Cannot read field: " + field.getName(), exception);
        }
    }

    private String normalizeOperator(String operator) {
        if (isBlank(operator)) {
            throw new IllegalArgumentException("Policy operator is required");
        }

        return operator.trim().toUpperCase();
    }

    private boolean isEqual(Object actualValue, Object expectedValue) {
        return Objects.equals(toStringValue(actualValue), toStringValue(expectedValue));
    }

    private boolean contains(Object actualValue, Object expectedValue) {
        return toStringValue(actualValue).contains(toStringValue(expectedValue));
    }

    private int compareNumber(Object actualValue, Object expectedValue) {
        BigDecimal actual = toBigDecimal(actualValue);
        BigDecimal expected = toBigDecimal(expectedValue);

        return actual.compareTo(expected);
    }

    private BigDecimal toBigDecimal(Object value) {
        return switch (value) {
            case null -> throw new IllegalArgumentException("Numeric operand is null");
            case BigDecimal bigDecimal -> bigDecimal;
            case Number number -> new BigDecimal(number.toString());
            default -> new BigDecimal(value.toString().trim());
        };

    }

    private boolean isInList(Object actualValue, Object expectedValue) {
        if (expectedValue == null) {
            return false;
        }

        String actual = toStringValue(actualValue);

        return Arrays.stream(toStringValue(expectedValue).split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .anyMatch(value -> value.equals(actual));
    }

    private String toStringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private boolean isDeny(String effect) {
        return EFFECT_DENY.equalsIgnoreCase(effect);
    }

    private boolean isAllow(String effect) {
        return EFFECT_ALLOW.equalsIgnoreCase(effect);
    }


    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

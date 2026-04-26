package org.example.springsecurity.handlers;

import org.example.springsecurity.configurations.security.UserInfo;
import org.example.springsecurity.models.PolicyRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class PolicyEvaluator {

    /**
     * Đánh giá danh sách rule theo ngữ nghĩa ABAC: DENY thắng, sau đó cần ít nhất 1 ALLOW
     * khớp khi có rule ALLOW. Không có rule nào → permit (RBAC đã pass ở caller).
     */
    public boolean evaluate(UserInfo user, Object resource, List<PolicyRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return true;
        }
        boolean hasAllow = false;
        boolean allowMatched = false;
        for (PolicyRule rule : rules) {
            boolean matched = evaluateRule(user, resource, rule);
            if ("DENY".equalsIgnoreCase(rule.getEffect())) {
                if (matched) {
                    return false;
                }
            } else {
                hasAllow = true;
                if (matched) {
                    allowMatched = true;
                }
            }
        }
        return !hasAllow || allowMatched;
    }

    private boolean evaluateRule(UserInfo user, Object resource, PolicyRule rule) {
        Object left = getValue(user, rule.getSubjectAttribute());
        Object right = rule.getExpectedValue() != null
                ? rule.getExpectedValue()
                : getValue(resource, rule.getResourceAttribute());

        return switch (rule.getOperator()) {
            case "EQ" -> Objects.equals(asString(left), asString(right));
            case "NE" -> !Objects.equals(asString(left), asString(right));
            case "GT" -> compareNumeric(left, right) > 0;
            case "GTE" -> compareNumeric(left, right) >= 0;
            case "LT" -> compareNumeric(left, right) < 0;
            case "LTE" -> compareNumeric(left, right) <= 0;
            case "IN" -> inList(left, right);
            case "NOT_IN" -> !inList(left, right);
            case "CONTAINS" -> asString(left).contains(asString(right));
            default -> throw new IllegalArgumentException("Unsupported operator: " + rule.getOperator());
        };
    }

    private Object getValue(Object target, String fieldName) {
        if (target == null || fieldName == null || fieldName.isBlank()) {
            return null;
        }
        if (target instanceof Map<?, ?> map) {
            return map.get(fieldName);
        }
        Class<?> clazz = target.getClass();
        while (clazz != null && clazz != Object.class) {
            try {
                var field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(target);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Cannot read field: " + fieldName, e);
            }
        }
        return null;
    }

    private static String asString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private static int compareNumeric(Object left, Object right) {
        BigDecimal l = toBigDecimal(left);
        BigDecimal r = toBigDecimal(right);
        return l.compareTo(r);
    }

    private static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Numeric operand is null");
        }
        if (value instanceof BigDecimal bd) {
            return bd;
        }
        if (value instanceof Number n) {
            return new BigDecimal(n.toString());
        }
        return new BigDecimal(value.toString().trim());
    }

    private static boolean inList(Object left, Object right) {
        if (right == null) {
            return false;
        }
        List<String> values = Arrays.stream(asString(right).split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        return values.contains(asString(left));
    }
}

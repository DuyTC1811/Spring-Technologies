package org.example.springsecurity.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.springsecurity.models.PolicyRule;

import java.util.List;

@Mapper
public interface IPolicyMapper {

    /**
     * Lấy toàn bộ rule (cùng effect của policy chứa nó) gắn với 1 permission code.
     * Chỉ trả về rule + policy đang enabled. Effect được copy từ bảng policies xuống
     * mỗi rule để evaluator quyết định ALLOW/DENY ở mức rule.
     */
    @Select("""
            SELECT pr.subject_attribute  AS subjectAttribute,
                   pr.operator           AS operator,
                   pr.resource_attribute AS resourceAttribute,
                   pr.expected_value     AS expectedValue,
                   p.effect              AS effect
            FROM permission_policies pp
            JOIN policies p       ON p.policy_id = pp.policy_id  AND p.enabled  = TRUE
            JOIN policy_rules pr  ON pr.policy_id = p.policy_id  AND pr.enabled = TRUE
            JOIN permissions per  ON per.per_id   = pp.per_id    AND per.enabled = TRUE
            WHERE pp.enabled = TRUE
              AND per.per_code = #{permissionCode}
            """)
    List<PolicyRule> findRulesByPermissionCode(@Param("permissionCode") String permissionCode);
}

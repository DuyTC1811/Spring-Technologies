package org.example.springsecurity.models;

import lombok.Data;

@Data
public class PolicyRule {

    private String ruleId;
    private String policyId;

    /**
     * Lấy từ bảng policies.
     */
    private String policyCode;

    /**
     * Lấy từ bảng policies.
     * ALLOW hoặc DENY.
     */
    private String effect;

    /**
     * Field trong UserInfo.
     * Ví dụ: userId, branchCode, departmentCode.
     */
    private String subjectAttribute;

    /**
     * EQ, NE, GT, GTE, LT, LTE, IN, NOT_IN, CONTAINS.
     */
    private String operator;

    /**
     * Field trong request/resource.
     * Ví dụ: ownerUserId, branchCode, amount.
     */
    private String resourceAttribute;

    /**
     * Giá trị cố định để so sánh.
     * Nếu có expectedValue thì ưu tiên dùng expectedValue.
     * Nếu null thì lấy resourceAttribute từ resource.
     */
    private String expectedValue;

    private String description;
}

package org.example.springsecurity.models;

import lombok.Data;

@Data
public class PolicyRule {
    private String effect;
    private String subjectAttribute;
    private String operator;
    private String resourceAttribute;
    private String expectedValue;
}

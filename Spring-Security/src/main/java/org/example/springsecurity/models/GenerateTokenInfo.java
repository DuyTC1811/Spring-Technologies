package org.example.springsecurity.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenerateTokenInfo {
    private String username;
    private String email;
    private String phone;
}

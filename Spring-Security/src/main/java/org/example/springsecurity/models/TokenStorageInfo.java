package org.example.springsecurity.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenStorageInfo {
    private String username;
    private String email;
    private String mobile;
    private String status;
    private int coutRefresh;
}

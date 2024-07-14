package org.example.springsecurity.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenStorageInfo {
    private String tokenId;
    private String asset_token;
    private String refresh_token;
    private String active;
}

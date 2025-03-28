package org.example.springsecurity.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenerateTokenInfo {
    /** UUID */
    private String uuid;

    /** username */
    private String username;

    /** accessKey */
    private String accessKey;

    /** accessExpireTime */
    private int accessExpireTime;

    /** refreshKey */
    private String refreshKey;

    /** refreshExpireTime */
    private int refreshExpireTime;

    /** verifiedKey */
    private String verifiedKey;

    /** verifiedExpireTime */
    private int verifiedExpireTime;

    /** version */
    private int version;
}

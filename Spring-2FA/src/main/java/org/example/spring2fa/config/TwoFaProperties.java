package org.example.spring2fa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "twofa")
public record TwoFaProperties(
        String applicationName,
        int time
) {
}

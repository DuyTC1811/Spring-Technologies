package org.example.springsecurity.configurations.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "cors")
public class PermitAllProperties {
    private AllowedProperties allowed;
    private PermitAll permitAll;

    @Getter
    @Setter
    public static class PermitAll {
        private String endpoint;
    }
}


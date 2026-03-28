package org.example.springsecurity.configurations.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private AllowedProperties allowed;
    private PermitAllProperties permitAll;
}


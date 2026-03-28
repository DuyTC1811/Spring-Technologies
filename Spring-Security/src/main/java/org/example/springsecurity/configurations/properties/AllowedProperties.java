package org.example.springsecurity.configurations.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllowedProperties {
    private List<String> origins;
    private List<String> methods;
    private List<String> headers;
}

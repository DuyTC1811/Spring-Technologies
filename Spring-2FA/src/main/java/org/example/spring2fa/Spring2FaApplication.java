package org.example.spring2fa;

import org.example.spring2fa.config.TwoFaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({TwoFaProperties.class})
public class Spring2FaApplication {

    public static void main(String[] args) {
        SpringApplication.run(Spring2FaApplication.class, args);
    }

}

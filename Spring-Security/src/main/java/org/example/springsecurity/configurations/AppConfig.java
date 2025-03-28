package org.example.springsecurity.configurations;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.example.springsecurity.configurations.caffeine.CacheValueWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Cache<String, CacheValueWrapper<String>> cache() {
        return Caffeine.newBuilder()
                .maximumSize(500)
                .build();
    }
}

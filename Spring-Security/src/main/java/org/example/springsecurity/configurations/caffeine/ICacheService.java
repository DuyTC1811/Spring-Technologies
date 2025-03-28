package org.example.springsecurity.configurations.caffeine;

public interface ICacheService {
    void putCache(String key, String value, Long duration);

    String getCache(String key);
}

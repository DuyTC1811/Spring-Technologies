package com.example.springrediscache.service;

import java.time.Duration;

public interface IRedisCacheService {
    <T> T getValue(String key, Class<T> clazz);

    void setValue(String key, Object value, Duration timeout);
}

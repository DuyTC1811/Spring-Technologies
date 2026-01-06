package org.example.ssestreaming.service;

import java.time.Duration;

public interface IRedisService {
    void setValue(String key, Object value, Duration ttl);

    <T> T getValue(String key, Class<T> type);

    void deleteValue(String key);
}

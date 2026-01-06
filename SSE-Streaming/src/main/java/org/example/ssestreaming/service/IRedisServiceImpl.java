package org.example.ssestreaming.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class IRedisServiceImpl implements IRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Optional: set with TTL
     */
    @Override
    public void setValue(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return type.cast(value);
    }


    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

}

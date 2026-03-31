package com.example.springrediscache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements IRedisCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {
            return clazz.cast(object);
        } else {
            log.info("CACHE IS EMPTY [{}]", key);
        }
        return null;
    }

    @Override
    public void setValue(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }
}

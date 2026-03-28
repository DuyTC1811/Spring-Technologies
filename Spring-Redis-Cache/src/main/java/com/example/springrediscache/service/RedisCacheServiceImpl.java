package com.example.springrediscache.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements IRedisCacheService {
    private final RedisTemplate<String, String> redisTemplate;
    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        return null;
    }
}

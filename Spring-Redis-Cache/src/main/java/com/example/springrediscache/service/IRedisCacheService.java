package com.example.springrediscache.service;

public interface IRedisCacheService {
    <T> T getValue(String key, Class<T> clazz);
}

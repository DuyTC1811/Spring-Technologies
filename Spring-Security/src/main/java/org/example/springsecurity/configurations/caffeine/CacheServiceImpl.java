package org.example.springsecurity.configurations.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheServiceImpl implements ICacheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheServiceImpl.class);
    private final Cache<String, CacheValueWrapper<String>> cache;

    @Override
    public void putCache(String key, String value, Long duration) {
        CacheValueWrapper<String> cacheValue = new CacheValueWrapper<>(value, duration);
        cache.put(key, cacheValue);
        LOGGER.info("[ PUT CACHE ] - with key: {} and value: {} duration: {} minutes", key, value, duration);
    }

    @Override
    public String getCache(String key) {
        CacheValueWrapper<String> cacheValue = cache.getIfPresent(key);

        if (cacheValue == null) {
            LOGGER.info("[ GET CACHE ] - KEY: {} not found in cache", key);
            return "";
        }

        // Check cache expired time
        if (cacheValue.isExpired()) {
            cache.invalidate(key);  // delete cache if expired
            LOGGER.info("[ GET CACHE ] - KEY: {} is expired and removed", key);
            return "";
        }

        String value = cacheValue.getValue();
        LOGGER.info("[ GET CACHE ] - KEY: {} VALUE: {}", key, value);
        return value;
    }
}

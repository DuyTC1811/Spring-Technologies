package org.example.springsecurity.configurations.caffeine;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CacheValueWrapper<T> {
    private final T value;
    private final LocalDateTime expirationTime;

    public CacheValueWrapper(T value, Long durationInMinutes) {
        this.value = value;
        this.expirationTime = LocalDateTime.now().plusMinutes(durationInMinutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }
}

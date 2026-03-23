package org.example.spring2fa.service;

import org.example.spring2fa.demo.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryUserRepository {
    private final Map<String, User> storage = new ConcurrentHashMap<>();

    public Optional<User> findById(String username) {
        return Optional.ofNullable(storage.get(username));
    }

    public User save(User user) {
        storage.put(user.getUsername(), user);
        return user;
    }
}

package org.example.springpdf.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class StrategyExecutor<T, R> {
    private final String name;
    private final Map<String, Strategy<T, R>> strategies;

    public StrategyExecutor(String name, List<? extends Strategy<T, R>> list) {
        this.name = Objects.requireNonNull(name, "name");
        Objects.requireNonNull(list, "strategies");

        if (list.isEmpty()) {
            throw new IllegalStateException(
                    "No strategies registered for executor '" + name + "'");
        }

        Map<String, Strategy<T, R>> map = new LinkedHashMap<>();
        for (Strategy<T, R> s : list) {
            String key = normalize(s.type(), s);
            Strategy<T, R> prev = map.putIfAbsent(key, s);
            if (prev != null) {
                throw new IllegalStateException(
                        "Duplicate strategy type '" + key + "' in executor '" + name + "': " +
                                prev.getClass().getName() + " vs " + s.getClass().getName());
            }
        }
        this.strategies = Collections.unmodifiableMap(map);
    }

    public R execute(String type, T input) {
        Objects.requireNonNull(input, "input");
        Strategy<T, R> s = strategies.get(normalize(type, null));
        if (s == null) {
            throw new IllegalArgumentException(
                    "No strategy '" + type + "' in executor '" + name +
                            "'. Available: " + strategies.keySet());
        }
        return s.execute(input);
    }

    public boolean supports(String type) {
        return type != null && strategies.containsKey(normalize(type, null));
    }

    public Set<String> supportedTypes() {
        return strategies.keySet();
    }

    public String name() {
        return name;
    }

    private static String normalize(String type, Object owner) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException(
                    "Strategy type must not be blank" +
                            (owner != null ? " (from " + owner.getClass().getName() + ")" : ""));
        }
        return type.trim().toUpperCase(Locale.ROOT);
    }
}

package org.example.ssestreaming.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public class SseEventUtils {
    public static SseEmitter.SseEventBuilder build(String name, Object data) {
        return SseEmitter.event()
                .id(UUID.randomUUID().toString())
                .name(name)
                .data(data);
    }
}

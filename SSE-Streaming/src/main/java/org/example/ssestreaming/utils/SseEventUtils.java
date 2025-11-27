package org.example.ssestreaming.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.atomic.AtomicLong;

public class SseEventUtils {
    // ID tự tăng thread-safe
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    /**
     * Tạo SseEvent với id tự tăng, name và data truyền vào.
     *
     * @param name tên event
     * @param data payload data
     * @return SseEventBuilder
     */
    public static SseEmitter.SseEventBuilder build(String name, Object data) {
        long id = ID_GENERATOR.getAndIncrement();

        return SseEmitter.event()
                .id(String.valueOf(id))
                .name(name)
                .data(data);
    }
}

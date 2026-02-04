package org.example.ssestreaming.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEventService {
    SseEmitter.SseEventBuilder buildEvent(String name, Object data);

    void pushEvent(SseEmitter emitter, Object data);

    SseEmitter handleUploadProgress(String streamId);
}

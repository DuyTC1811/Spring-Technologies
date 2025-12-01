package org.example.ssestreaming.common;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseProgressStore {
    private final Map<String, UploadStatus> store = new ConcurrentHashMap<>();

    public UploadStatus get(String uploadId) {
        return store.get(uploadId);
    }

    public void save(UploadStatus status) {
        store.put(status.getUploadId(), status);
    }

    public void remove(String uploadId) {
        store.remove(uploadId);
    }
}

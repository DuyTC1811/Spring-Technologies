package org.example.ssestreaming.common;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UploadService {
    private final SseProgressStore statusStore;

    public UploadService(SseProgressStore statusStore) {
        this.statusStore = statusStore;
    }

    @Async("virtualThread")
    public void processFileAsync(String uploadId, MultipartFile files) {

        // Khởi tạo trạng thái ban đầu
        UploadStatus init = new UploadStatus();
        init.setUploadId(uploadId);
        init.setState(UploadState.PENDING);
        init.setMessage("Waiting to start...");
        statusStore.save(init);

        UploadStatus status = statusStore.get(uploadId);

        long lastPushTime = 0;   // thời điểm đẩy SSE gần nhất (ms)

        try {
            Thread.sleep(100);

            status.setState(UploadState.RUNNING);
            status.setMessage("Processing file...");
            statusStore.save(status);

            // Lấy totalRows thật sau này
            int totalRows = 100;
            status.setTotalRows(totalRows);
            statusStore.save(status);

            lastPushTime = System.currentTimeMillis();

            for (int i = 1; i <= totalRows; i++) {

                Thread.sleep(400);

                status.setProcessedRows(i);

                if (i % 10 == 0) {
                    status.setFailedRows(status.getFailedRows() + 1);
                    status.setMessage("Row " + i + " failed");
                } else {
                    status.setSuccessRows(status.getSuccessRows() + 1);
                    status.setMessage("Row " + i + " processed");
                }

                statusStore.save(status);

                // ⏱️ Chỉ gửi SSE mỗi 1 giây
                long now = System.currentTimeMillis();
                if (now - lastPushTime >= 1000) {
                    statusStore.save(status);
                    lastPushTime = now;
                }
            }

            // Gửi final update khi hoàn thành
            status.setState(UploadState.SUCCESS);
            status.setMessage("Upload completed!");
            statusStore.save(status);

        } catch (Exception ex) {
            status.setState(UploadState.FAILED);
            status.setMessage("Error: " + ex.getMessage());
            statusStore.save(status);
        }
    }

}

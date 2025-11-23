package org.example.ssestreaming.common;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UploadService {
    private final SseProgressStore statusStore;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public UploadService(SseProgressStore statusStore) {
        this.statusStore = statusStore;
    }

    public void processFileAsync(String uploadId) {

        // Khởi tạo trạng thái ban đầu
        UploadStatus init = new UploadStatus();
        init.setUploadId(uploadId);
        init.setState(UploadState.PENDING);
        init.setMessage("Waiting to start...");
        statusStore.save(init);

        executor.submit(() -> {
            UploadStatus status = statusStore.get(uploadId);

            try {
                // 🔥 Delay lớn trước khi bắt đầu
                Thread.sleep(1500);

                status.setState(UploadState.RUNNING);
                status.setMessage("Processing file...");
                statusStore.save(status);

                // TODO đọc excel thật → lấy totalRows thật
                int totalRows = 100;  // giả lập test
                status.setTotalRows(totalRows);
                statusStore.save(status);

                for (int i = 1; i <= totalRows; i++) {

                    // 🔥 Delay từng dòng (chậm hơn để test)
                    Thread.sleep(400);

                    status.setProcessedRows(i);

                    // kiểm tra OK / lỗi
                    if (i % 10 == 0) {
                        status.setFailedRows(status.getFailedRows() + 1);
                        status.setMessage("Row " + i + " failed");
                    } else {
                        status.setSuccessRows(status.getSuccessRows() + 1);
                        status.setMessage("Row " + i + " processed");
                    }

                    statusStore.save(status);
                }

                status.setState(UploadState.SUCCESS);
                status.setMessage("Upload completed!");
                statusStore.save(status);

            } catch (Exception ex) {
                status.setState(UploadState.FAILED);
                status.setMessage("Error: " + ex.getMessage());
                statusStore.save(status);
            }
        });
    }
}

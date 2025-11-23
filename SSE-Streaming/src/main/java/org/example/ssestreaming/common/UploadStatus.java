package org.example.ssestreaming.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadStatus {
    private String uploadId;
    private int totalRows;
    private int processedRows;
    private int successRows;
    private int failedRows;
    private UploadState state;
    private String message;     // thông tin tổng quát, ví dụ: "Processing", "Done", "Error: ..."
    // TODO: có thể thêm List<ErrorRow> errors;

    public int getProgressPercent() {
        if (totalRows == 0) return 0;
        return (int) ((processedRows * 100.0) / totalRows);
    }
}

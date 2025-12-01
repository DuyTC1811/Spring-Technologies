package org.example.ssestreaming.enums;

public enum UploadState {
    PROCESSING,         // Đang xử lý chung
    EXTRACTING,         // Đang extract e-invoice
    EXTRACT_ERROR,      // Lỗi extract file
    TAXCODE_NOT_MATCH,  // MST KH không khớp MST người mua trên HĐ
    EXTRACTED,          // Đã extract thành công, show thông tin hóa đơn
    VALID,              // Hóa đơn hợp lệ (sau các bước check)
    FAILED,             // Có lỗi, dừng
    SUCCESS             // Thành công hoàn toàn
}

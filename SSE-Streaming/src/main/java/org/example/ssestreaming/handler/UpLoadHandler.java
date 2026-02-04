package org.example.ssestreaming.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ssestreaming.common.SseProgressStore;
import org.example.ssestreaming.common.UploadStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.example.ssestreaming.enums.UploadState.EXTRACTED;
import static org.example.ssestreaming.enums.UploadState.EXTRACTING;
import static org.example.ssestreaming.enums.UploadState.EXTRACT_ERROR;
import static org.example.ssestreaming.enums.UploadState.FAILED;
import static org.example.ssestreaming.enums.UploadState.PROCESSING;
import static org.example.ssestreaming.enums.UploadState.SUCCESS;
import static org.example.ssestreaming.enums.UploadState.TAXCODE_NOT_MATCH;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpLoadHandler {
    private final SseProgressStore sseProgressStore;

    @Async("virtualThread")
    public void handlerUpload(String uploadId, MultipartFile file) {
        try {
            log.info("[{}] START background processing ", file.getOriginalFilename());

            // STEP 1 – INIT (0%)
            sseProgressStore.save(new UploadStatus(uploadId, 0, PROCESSING, "Bắt đầu upload hóa đơn điện tử"));
            Thread.sleep(5_000L);

            // STEP 2 – validate file (fake)
            // Ở đây em có thể random lỗi để FE test
            sseProgressStore.save(new UploadStatus(uploadId, 10, PROCESSING, "Đang kiểm tra định dạng file XML"));
            Thread.sleep(5_000L);

            // STEP 3 – EXTRACTING
            sseProgressStore.save(new UploadStatus(uploadId, 30, EXTRACTING, "Đang đọc dữ liệu hóa đơn điện tử"));
            Thread.sleep(5_000L);

            // Giả lập 2 case:
            boolean extractError = false; // đổi sang true để test flow lỗi extract
            if (extractError) {
                sseProgressStore.save(new UploadStatus(uploadId, 40, EXTRACT_ERROR, "Không đọc được dữ liệu, cấu trúc hóa đơn không hợp lệ"));
                return; // kết thúc luôn
            }

            // STEP 4 – Đã EXTRACTED, show thông tin hóa đơn (số hóa đơn, tiền, MST…)
            sseProgressStore.save(new UploadStatus(uploadId, 50, EXTRACTED, "Đã đọc được thông tin hóa đơn, chờ kiểm tra MST"));
            Thread.sleep(5_000L);

            // STEP 5 – Kiểm tra MST KH với MST người mua trên HĐ
            boolean taxcodeNotMatch = false; // set true để test case TAXCODE_NOT_MATCH
            if (taxcodeNotMatch) {
                sseProgressStore.save(new UploadStatus(uploadId, 60, TAXCODE_NOT_MATCH, "MST khách hàng không trùng với MST người mua trên hóa đơn"));
                // Có thể cho FE sửa MST rồi bấm lại, nên không SUCCESS/FAILED ngay
                return;
            }

            // STEP 6 – Gắn e-invoice vào checklist (mock)
            sseProgressStore.save(new UploadStatus(uploadId, 80, PROCESSING, "Đang gắn hóa đơn vào checklist"));
            Thread.sleep(5_000L);

            // STEP 7 – Hoàn tất, trạng thái VALID + SUCCESS
            sseProgressStore.save(new UploadStatus(uploadId, 95, PROCESSING, "Hóa đơn hợp lệ, chuẩn bị hoàn tất"));
            Thread.sleep(5_000L);

            sseProgressStore.save(new UploadStatus(uploadId, 100, SUCCESS, "Upload hóa đơn điện tử thành công"));
            log.info("[{}] SUCCESS 100%", uploadId);

        } catch (Exception ex) {
            log.error("[{}] FAILED: {}", uploadId, ex.getMessage(), ex);
            sseProgressStore.save(new UploadStatus(uploadId, 0, FAILED, "Có lỗi hệ thống trong quá trình xử lý hóa đơn"));
            Thread.currentThread().interrupt();
        }

    }
}

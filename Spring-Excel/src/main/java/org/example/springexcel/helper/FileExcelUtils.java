package org.example.springexcel.helper;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class FileExcelUtils {
        // ====== Constants ======
        private static final String SHEET_NAME = "Beneficiaries";
        private static final int HEADER_ROW = 0;
        private static final int START_ROW = 1;
        private static final int MAX_TEMPLATE_ROWS = 100; // vùng dropdown
        private static final int COL_STT = 0;
        private static final int COL_TRANSFER = 1;
        private static final int COL_PURPOSE = 2;
        private static final int COL_AMOUNT = 3;
        private static final int COL_BEN_NAME = 4;
        private static final int COL_TAX = 5;
        private static final int COL_BANK = 6;
        private static final int COL_ACC = 7;
        private static final int COL_CONTENT = 8;
        private static final int COL_NOTE = 9;

        // Cột -> width (đơn vị: 1/256 ký tự, ~256 là 1 ký tự)
        private static final int[] COL_WIDTHS = {
                6 * 256,  // STT
                26 * 256, // Loại chuyển khoản
                24 * 256, // Mục đích thanh toán
                16 * 256, // Số tiền
                26 * 256, // Tên thụ hưởng
                16 * 256, // Mã số thuế
                18 * 256, // Ngân hàng hưởng
                20 * 256, // Số tài khoản
                30 * 256, // Nội dung
                20 * 256  // Note
        };

        private static final String[] TRANSFER_TYPES = {
                "Chuyển thường", "Chuyển nhanh 24/7", "Chuyển nhanh", "Trong MSB"
        };

        /**
         * Xuất template Excel tối ưu (ghi tuần tự – an toàn & nhanh).
         * Chuẩn bị dữ liệu (nếu nhiều) có thể làm song song ở ngoài, rồi truyền vào đây để ghi.
         */
        public byte[] exportTemplateBytes() {
            // Tạo XSSFWorkbook gốc để bật shared strings table khi wrap sang SXSSF
            try (XSSFWorkbook xssf = new XSSFWorkbook();
                 SXSSFWorkbook wb = new SXSSFWorkbook(xssf, /*rowWindow*/ 500, /*compressTmp*/ true, /*useSharedStrings*/ true);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream(64 * 1024)) {

                wb.setCompressTempFiles(true);

                Sheet sh = wb.createSheet(SHEET_NAME);
                // Freeze header
                sh.createFreezePane(0, 1);
                // Set width cột cố định (rẻ hơn autosize rất nhiều)
                for (int c = 0; c < COL_WIDTHS.length; c++) {
                    sh.setColumnWidth(c, COL_WIDTHS[c]);
                }

                // ===== Styles (tạo 1 lần, tái sử dụng) =====
                // Header
                CellStyle headerStyle = wb.createCellStyle();
                Font headerFont = wb.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setWrapText(true);
                // ✅ Thêm màu nền xanh lam nhạt
                headerStyle.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // Viền các bên
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);

                headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);

                // Amount style
                CellStyle amountStyle = wb.createCellStyle();
                DataFormat fmt = wb.createDataFormat();
                amountStyle.setDataFormat(fmt.getFormat("#,##0"));

                // Text wrap style (optional)
                CellStyle wrapStyle = wb.createCellStyle();
                wrapStyle.setWrapText(true);

                // ===== Header row =====
                Row h = sh.createRow(HEADER_ROW);
                putString(h, COL_STT, "STT", headerStyle);
                putString(h, COL_TRANSFER, "Loại chuyển khoản", headerStyle);
                putString(h, COL_PURPOSE, "Mục đích thanh toán", headerStyle);
                putString(h, COL_AMOUNT, "Số tiền", headerStyle);
                putString(h, COL_BEN_NAME, "Tên thụ hưởng", headerStyle);
                putString(h, COL_TAX, "Mã số thuế", headerStyle);
                putString(h, COL_BANK, "Ngân hàng hưởng", headerStyle);
                putString(h, COL_ACC, "Số tài khoản", headerStyle);
                putString(h, COL_CONTENT, "Nội dung", headerStyle);
                putString(h, COL_NOTE, "Note", headerStyle);
                h.setHeightInPoints(22);

                // ===== Sample row (1 dòng minh họa) =====
                Row r1 = sh.createRow(START_ROW);
                putLong(r1, COL_STT, 1);
                putString(r1, COL_TRANSFER, "Chuyển nhanh 24/7", null);
                putString(r1, COL_PURPOSE, "Khác", null);
                putLong(r1, COL_AMOUNT, 150_000L, amountStyle);
                putString(r1, COL_BEN_NAME, "NGUYEN VAN A", null);
                putString(r1, COL_TAX, "0102030405", null);
                putString(r1, COL_BANK, "MSB", null);
                putString(r1, COL_ACC, "0123456789", null);
                putString(r1, COL_CONTENT, "Thanh toan hoa don", wrapStyle);
                putString(r1, COL_NOTE, "", null);

                // ===== Data Validation (dropdown cho cột Loại chuyển khoản) =====
                addDropdown(sh, START_ROW, START_ROW + MAX_TEMPLATE_ROWS, COL_TRANSFER, COL_TRANSFER, TRANSFER_TYPES);

                wb.write(bos);
                return bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("Không thể xuất Excel", e);
            }
        }

        // ===================== Helpers =====================

        private static void putString(Row row, int col, String v, CellStyle style) {
            Cell cell = row.createCell(col, CellType.STRING);
            cell.setCellValue(v == null ? "" : v);
            if (style != null) {
                cell.setCellStyle(style);
            }
        }

        private static void putLong(Row row, int col, long v) {
            Cell cell = row.createCell(col, CellType.NUMERIC);
            cell.setCellValue(v);
        }

        private static void putLong(Row row, int col, long v, CellStyle style) {
            Cell cell = row.createCell(col, CellType.NUMERIC);
            cell.setCellValue(v);
            if (style != null) cell.setCellStyle(style);
        }

        private static void addDropdown(Sheet sh, int firstRow, int lastRow, int firstCol, int lastCol, String[] items) {
            DataValidationHelper helper = sh.getDataValidationHelper();
            DataValidationConstraint cons = helper.createExplicitListConstraint(items);
            CellRangeAddressList range = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            DataValidation dv = helper.createValidation(cons, range);
            dv.setSuppressDropDownArrow(true);
            dv.setShowErrorBox(true);
            sh.addValidationData(dv);
        }



}

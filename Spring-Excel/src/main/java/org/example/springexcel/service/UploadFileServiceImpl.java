package org.example.springexcel.service;//        long start = System.nanoTime();
//        for (MultipartFile file : files) {
//            try (var inputStream = file.getInputStream(); var workbook = new ReadableWorkbook(inputStream)) {
//                Sheet sheet = workbook.getFirstSheet();
//                try (var rows = sheet.openStream()) {
//                    rows.skip(1).forEach(row -> {
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsDate(0).orElse(null));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(1).orElse(""));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(2).orElse(""));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(3).orElse(BigDecimal.ONE));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(4).orElse(BigDecimal.ONE));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(5).orElse(""));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(6).orElse(""));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(7).orElse(BigDecimal.ONE));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(8).orElse(""));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(9).orElse(BigDecimal.ONE));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(10).orElse(""));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(11).orElse(""));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(12).orElse(BigDecimal.ONE));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(13).orElse(""));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(14).orElse(""));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(15).orElse(BigDecimal.ONE));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsDate(16).orElse(null));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsDate(17).orElse(null));
//                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(18).orElse(""));
//                    });
//                }
//                long end = System.nanoTime();
//                long elapsedNs = end - start;
//                System.out.println("⏱ Read + validate time = " + (elapsedNs / 1_000_000) + " ms");
//            } catch (Exception e) {
//                System.err.println("Error processing file: " + file.getOriginalFilename());
//                e.printStackTrace(System.err);
//            }
//        }

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.example.springexcel.helper.RowMapper;
import org.example.springexcel.model.ErrorMess;
import org.example.springexcel.model.ResponseData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UploadFileServiceImpl implements IUploadFileService {
    private static final int[] REQUIRED_COLS = {1, 2, 3, 4, 5, 6, 7, 8};
    private static final String ACCOUNT_NO_REGEX = "^\\d{8,20}$";

    @Override
    public void uploadFiles(List<MultipartFile> files) {
        long start = System.nanoTime();
        for (MultipartFile file : files) {
            try (var inputStream = file.getInputStream(); var workbook = new ReadableWorkbook(inputStream)) {
                Sheet sheet = workbook.getFirstSheet();
                try (var rows = sheet.openStream()) {
                    rows.skip(1).limit(10).forEach(row -> {
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(1).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(2).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(3).orElse(null));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(4).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(5).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(6).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(7).orElse(null));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(8).orElse(""));
                    });
                }
                long end = System.nanoTime();
                long elapsedNs = end - start;
                System.out.println("⏱ Read + validate time = " + (elapsedNs / 1_000_000) + " ms");
            } catch (Exception e) {
                System.err.println("Error processing file: " + file.getOriginalFilename());
                e.printStackTrace(System.err);
            }
        }
    }

    @Override
    public List<ResponseData> readSingleFile(MultipartFile file) {
        try (var inputStream = file.getInputStream();
             var workbook = new ReadableWorkbook(inputStream)) {

            Sheet sheet = workbook.getFirstSheet();

            try (var rows = sheet.openStream()) {
                List<ResponseData> result = rows
                        .skip(1)
                        .takeWhile(row -> hasAnyValue(row, REQUIRED_COLS))
                        .map(rowMapper::map)
                        .toList();

                if (CollectionUtils.isEmpty(result)) {
                    throw new RuntimeException("Excel file is empty or contains no data");
                }
                return result;
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Read excel failed", e);
        }
    }

    public RowMapper<ResponseData> rowMapper = row -> {
        ResponseData data = new ResponseData();
        data.setRowNum(row.getRowNum());
        data.setCol1(readString(row, 1, data, "col1"));
        data.setCol2(readString(row, 2, data, "col2"));
        data.setCol3(readNumber(row, 3, data, "col3"));
        data.setCol4(readString(row, 4, data, "col4"));
        data.setCol5(readString(row, 5, data, "col5"));
        data.setCol6(readString(row, 6, data, "col6"));
        data.setCol7(readAccountNumber(row, 7, data));
        data.setCol8(readString(row, 8, data, "col8"));
        return data;
    };

    public static boolean hasAnyValue(Row row, int... cols) {
        for (int col : cols) {
            if (row.getCellAsString(col).
                    filter(s -> !s.isBlank()).isPresent() || row.getCellAsNumber(col).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public static String readString(Row row, int col, ResponseData data, String colName) {
        try {
            return row.getCellAsString(col).orElse("");
        } catch (Exception e) {
            data.getErrors().add(
                    new ErrorMess(row.getRowNum(), colName, "Invalid string format")
            );
            return "";
        }
    }

    public static BigDecimal readNumber(Row row, int col, ResponseData data, String colName) {
        try {
            return row.getCellAsNumber(col).orElse(null);
        } catch (Exception e) {
            data.getErrors().add(
                    new ErrorMess(row.getRowNum(), colName, "Invalid number format")
            );
            return null;
        }
    }

    public static String readAccountNumber(Row row, int col, ResponseData data) {
        String value = row.getCellAsNumber(col)
                .map(BigDecimal::toPlainString)
                .orElseGet(() -> row.getCellAsString(col)
                        .map(String::strip)
                        .orElse("")
                );

        if (value.isEmpty()) {
            data.getErrors().add(new ErrorMess(row.getRowNum() ," 7 ", "Account number is empty"));
            return value;
        }

        // Validate format số tài khoản
        if (!value.matches(ACCOUNT_NO_REGEX)) {
            data.getErrors().add(new ErrorMess(row.getRowNum(), " 7 " ,"Invalid account number format: " + value));
        }
        return value;
    }



    private String getString(Cell cell) {
        if (cell == null) {
            return null;
        }
        return cell.getStringCellValue().trim();
    }

    private LocalDate getLocalDate(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        return null;
    }

    private BigDecimal getBigDecimal(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;

        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }

        if (cell.getCellType() == CellType.STRING) {
            return new BigDecimal(cell.getStringCellValue().trim());
        }

        return BigDecimal.ZERO;
    }

}

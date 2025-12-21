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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class UploadFileServiceImpl implements IUploadFileService {
    @Override
    public void uploadFiles(List<MultipartFile> files) {
        long start = System.nanoTime();
        for (MultipartFile file : files) {
            try (var inputStream = file.getInputStream(); var workbook = new ReadableWorkbook(inputStream)) {
                Sheet sheet = workbook.getFirstSheet();
                try (var rows = sheet.openStream()) {
                    rows.skip(1).forEach(row -> {
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsDate(0).orElse(null));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(1).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(2).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(3).orElse(BigDecimal.ONE));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(4).orElse(BigDecimal.ONE));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(5).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(6).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(7).orElse(BigDecimal.ONE));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(8).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(9).orElse(BigDecimal.ONE));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(10).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(11).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(12).orElse(BigDecimal.ONE));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(13).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(14).orElse(""));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsNumber(15).orElse(BigDecimal.ONE));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsDate(16).orElse(null));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsDate(17).orElse(null));
                        System.out.println("Row " + row.getRowNum() + ": " + row.getCellAsString(18).orElse(""));
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
    public void readSingleFile(List<MultipartFile> files) {
//        long start = System.nanoTime();
//        for (MultipartFile file : files) {
//            try (InputStream in = file.getInputStream(); Workbook workbook = new XSSFWorkbook(in)) {
//                Sheet sheet = workbook.getSheetAt(0);
//                DataFormatter formatter = new DataFormatter();
//
//                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
//                    Row row = sheet.getRow(r);
//                    if (row.getRowNum() == 0) {
//                        continue; // skip header
//                    }
//
//                    LocalDate column0 = getLocalDate(row.getCell(0));
//                    System.out.println("Row " + row.getRowNum() + ": " + column0);
//                    String column1 = getString(row.getCell(1));
//                    System.out.println("Row " + row.getRowNum() + ": " + column1);
//
//                }
//                long end = System.nanoTime();
//                long elapsedNs = end - start;
//                System.out.println("⏱ Read + validate time = " + (elapsedNs / 1_000_000) + " ms");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
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

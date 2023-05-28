package com.example.springexcel.helper;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JavaHelper {
    private final String EXCEL_FILE_PATH = "excel_file.xlsx";

    // Get cell value
    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = "";
        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                 cellValue = Integer.valueOf((int) cell.getNumericCellValue()) ;
                break;
            case STRING:
                cellValue = cell.getStringCellValue().strip().replaceAll("\\s+", " ");
                break;
            default:
                break;
        }
        return cellValue;
    }

    public List<Map<Object, Map<Object, Map<Object, Map<Object, Object>>>>> readExcel() throws IOException {
        List<Map<Object, Map<Object, Map<Object, Map<Object, Object>>>>> resultMap = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(EXCEL_FILE_PATH))) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Map<Object, Map<Object, Map<Object, Map<Object, Object>>>> map = new LinkedHashMap<>();
                Map<Object, Map<Object, Map<Object, Object>>> innerMap1 = new LinkedHashMap<>();
                Map<Object, Map<Object, Object>> innerMap2 = new LinkedHashMap<>();
                Map<Object, Object> innerMap3 = new LinkedHashMap<>();
                List<Object> objectList = new ArrayList<>();

                int rowNum = row.getRowNum();
                if (rowNum < 4) continue;
                if (rowNum > 16) break;

                Object value1 = getCellValue(row.getCell(0));
                Object value2 = getCellValue(row.getCell(1));
                Object value3 = getCellValue(row.getCell(2));
                Object value4 = getCellValue(row.getCell(3));
                Object value5 = getCellValue(row.getCell(4));
                Object value6 = getCellValue(row.getCell(5));
                Object value7 = getCellValue(row.getCell(6));
                Object value8 = getCellValue(row.getCell(7));
                Object value9 = getCellValue(row.getCell(8));
                Object value10 = getCellValue(row.getCell(9));
                Object value11 = getCellValue(row.getCell(10));
                Object value12 = getCellValue(row.getCell(11));

                innerMap3.put("Task (Small)", value3);
                innerMap3.put("Point", value5);
                innerMap3.put("Pic", value4);
                innerMap2.put(value2, innerMap3);
                innerMap1.put("Task (Medium)", innerMap2);
                map.put(value1, innerMap1);
                resultMap.add(map);
            }
            workbook.close();
        }
        return resultMap;
    }
}

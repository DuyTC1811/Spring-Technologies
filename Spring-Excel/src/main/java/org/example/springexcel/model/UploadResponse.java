package org.example.springexcel.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UploadResponse {
    private int validCount;
    private int invalidCount;
    private int totalCount;
    private List<ResponseData> validData = new ArrayList<>();
}

package org.example.springexcel.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseData {
    private int rowNum;
    private String col1;
    private String col2;
    private BigDecimal col3;
    private String col4;
    private String col5;
    private String col6;
    private String col7;
    private String col8;
    private List<ErrorMess> errors = new ArrayList<>();
}

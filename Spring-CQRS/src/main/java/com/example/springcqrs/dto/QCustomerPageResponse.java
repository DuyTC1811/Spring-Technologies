package com.example.springcqrs.dto;

import lombok.Data;

import java.util.Map;

@Data
public class QCustomerPageResponse {
    private int limit;
    private int offset;
    private int currentPage;
    private Map<String, Object> data;

}

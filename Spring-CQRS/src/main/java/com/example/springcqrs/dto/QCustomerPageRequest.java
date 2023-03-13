package com.example.springcqrs.dto;

import com.example.springcqrs.cqrs.query.IQuery;
import lombok.Data;

@Data
public class QCustomerPageRequest implements IQuery<QCustomerPageResponse> {
    private String name;
}

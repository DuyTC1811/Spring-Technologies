package com.example.springcqrs.dto;

import com.example.springcqrs.cqrs.query.IQuery;
import lombok.Data;

@Data
public class QCustomerRequest implements IQuery<QCustomerResponse> {
    private int id;
}

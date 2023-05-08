package com.example.springcqrs.dto;

import com.example.springcqrs.cqrs.query.IQuery;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QCustomerRequest implements IQuery<QCustomerResponse> {
    private int id;
}

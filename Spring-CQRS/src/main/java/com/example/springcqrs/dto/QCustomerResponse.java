package com.example.springcqrs.dto;

import com.example.springcqrs.entitys.Customer;
import lombok.Data;

@Data
public class QCustomerResponse {
    private int id;
    private String name;
    private int age;

    public static QCustomerResponse build(Customer customer) {
        QCustomerResponse response = new QCustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setAge(customer.getAge());
        return response;
    }
}

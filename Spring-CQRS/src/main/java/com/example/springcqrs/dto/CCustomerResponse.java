package com.example.springcqrs.dto;

import com.example.springcqrs.entitys.Customer;
import lombok.Data;

@Data
public class CCustomerResponse {
    private int id;
    private String name;

    public static CCustomerResponse build(Customer customer) {
        CCustomerResponse response = new CCustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        return response;
    }
}

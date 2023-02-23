package com.example.springcqrs.dto;

import com.example.springcqrs.cqrs.command.ICommand;
import lombok.Data;

@Data
public class CCustomerRequest implements ICommand<CCustomerResponse> {
    private int id;
    private String name;
    private int age;
}

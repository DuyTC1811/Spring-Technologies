package com.example.springcqrs.entitys;

import com.example.springcqrs.dto.CCustomerRequest;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "customer")
@Entity
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;

    public static Customer create(CCustomerRequest request) {
        Customer customer = new Customer();
        customer.setId(request.getId());
        customer.setName(request.getName());
        customer.setAge(request.getAge());
        return customer;
    }
}

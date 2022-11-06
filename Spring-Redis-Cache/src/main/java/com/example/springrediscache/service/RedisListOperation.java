package com.example.springrediscache.service;

import com.example.springrediscache.entity.Customer;
import com.example.springrediscache.entity.Range;

import java.util.List;

public interface RedisListOperation {
    void create(List<Customer> listData);

    List<Customer> listCustomer(Range range);

    Customer getId(Long index);

    void delete(Customer customer, int count);
}

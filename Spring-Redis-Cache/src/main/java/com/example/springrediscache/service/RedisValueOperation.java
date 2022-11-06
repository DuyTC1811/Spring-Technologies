package com.example.springrediscache.service;

import com.example.springrediscache.entity.Customer;

public interface RedisValueOperation {
    void create(Customer customer);

    void createSetTime(int time, Customer customer);

    Customer getValue();

    void deleted();
}

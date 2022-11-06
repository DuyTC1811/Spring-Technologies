package com.example.springrediscache.service;

import com.example.springrediscache.entity.Customer;

import java.util.Map;

public interface RedisHashOperation {
    void create(Customer customer);

    void update(Customer customer);

    Map<Integer, Customer> getAll();

    void delete(Integer id);

}

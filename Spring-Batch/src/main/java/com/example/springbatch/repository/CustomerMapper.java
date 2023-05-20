package com.example.springbatch.repository;

import com.example.springbatch.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper {
    void insert(Customer customer);
}

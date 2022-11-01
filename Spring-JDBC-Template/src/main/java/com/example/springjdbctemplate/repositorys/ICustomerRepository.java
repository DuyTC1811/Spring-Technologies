package com.example.springjdbctemplate.repositorys;

import com.example.springjdbctemplate.entitys.Customer;

import java.util.Map;

public interface ICustomerRepository {
    void save(Customer customer);

    void update(Customer customer);

    void delete(int customerId);

    Customer getById(Integer customerId);

    Map<String, Object> getPage(int page, int size);
}

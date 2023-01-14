package com.example.multidatabase.services;

import com.example.multidatabase.entity.CustomerPostgres;
import com.example.multidatabase.entity.CustomersMongo;

public interface ICustomerMongoService {
    CustomersMongo createCustomer(CustomersMongo customer);
    void deleteByIdCustomer(Integer customerId);
    CustomersMongo infoCustomer(String name);
}

package com.example.multidatabase.services;

import com.example.multidatabase.entity.CustomerPostgres;

public interface ICustomerPostgresService {
   CustomerPostgres createCustomer(CustomerPostgres customer);
   void deleteByIdCustomer(Integer customerId);
   CustomerPostgres infoCustomer(Integer customerId);
}

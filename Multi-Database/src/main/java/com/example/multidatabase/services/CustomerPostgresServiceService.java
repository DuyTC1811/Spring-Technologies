package com.example.multidatabase.services;

import com.example.multidatabase.entity.CustomerPostgres;
import com.example.multidatabase.repositorys.RepositoryPostgres;
import org.springframework.stereotype.Service;

@Service
public class CustomerPostgresServiceService implements ICustomerPostgresService {
    private final RepositoryPostgres repositoryPostgres;

    public CustomerPostgresServiceService(RepositoryPostgres repositoryPostgres) {
        this.repositoryPostgres = repositoryPostgres;
    }

    @Override
    public CustomerPostgres createCustomer(CustomerPostgres customer) {
        return repositoryPostgres.save(customer);
    }

    @Override
    public void deleteByIdCustomer(Integer customerId) {
        repositoryPostgres.deleteById(customerId);
    }

    @Override
    public CustomerPostgres infoCustomer(Integer customerId) {
        return repositoryPostgres.findByIds(customerId).orElse(new CustomerPostgres());
    }
}

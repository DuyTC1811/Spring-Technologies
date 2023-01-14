package com.example.multidatabase.services;

import com.example.multidatabase.entity.CustomerPostgres;
import com.example.multidatabase.entity.CustomersMongo;
import com.example.multidatabase.repositorys.RepositoryMongo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerMongoService implements ICustomerMongoService {
    private final RepositoryMongo repositoryMongo;
    private final MongoTemplate template;

    public CustomerMongoService(RepositoryMongo repositoryMongo, MongoTemplate template) {
        this.repositoryMongo = repositoryMongo;
        this.template = template;
    }

    @Override
    public CustomersMongo createCustomer(CustomersMongo customer) {
        return repositoryMongo.save(customer);
    }

    @Override
    public void deleteByIdCustomer(Integer customerId) {
        repositoryMongo.deleteById(customerId);
    }

    @Override
    public CustomersMongo infoCustomer(String name) {
        return repositoryMongo.findByName(name);
    }
}

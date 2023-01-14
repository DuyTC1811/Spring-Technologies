package com.example.multidatabase.repositorys;

import com.example.multidatabase.entity.CustomersMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryMongo extends MongoRepository<CustomersMongo, Integer> {
    @Query("{ 'name' : ?0 }")
    CustomersMongo findByName(String name);
}

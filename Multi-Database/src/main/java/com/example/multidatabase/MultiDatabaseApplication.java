package com.example.multidatabase;

import com.example.multidatabase.entity.CustomersMongo;
import com.example.multidatabase.repositorys.RepositoryMongo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.multidatabase.repositorys")
public class MultiDatabaseApplication {
    private final RepositoryMongo repositoryMongo;

    public MultiDatabaseApplication(RepositoryMongo repositoryMongo) {
        this.repositoryMongo = repositoryMongo;
    }

    public static void main(String[] args) {
        SpringApplication.run(MultiDatabaseApplication.class, args);
    }

    @PostConstruct
    public void addListMongo() {
        CustomersMongo customersMongo = new CustomersMongo();
        customersMongo.setId(1);
        customersMongo.setName("Duy TC");
        customersMongo.setAddress("Nam Dinh");
        repositoryMongo.save(customersMongo);
    }
}

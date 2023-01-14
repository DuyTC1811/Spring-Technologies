package com.example.multidatabase.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
@Data
@Document(collection = "customer")
public class CustomersMongo {
    @Id
    private int id;
    @Indexed(unique = true)
    private String name;
    private String address;
}

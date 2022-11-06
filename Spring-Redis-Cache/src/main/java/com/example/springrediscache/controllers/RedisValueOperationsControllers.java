package com.example.springrediscache.controllers;

import com.example.springrediscache.entity.Customer;
import com.example.springrediscache.service.RedisValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("value_operation")
public class RedisValueOperationsControllers {
    private final RedisValueOperations valueOperations;

    public RedisValueOperationsControllers(RedisValueOperations valueOperations) {
        this.valueOperations = valueOperations;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody Customer customer) {
        valueOperations.create(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> update(Customer customer) {
        valueOperations.create(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<Customer> getInfo() {
        Customer value = valueOperations.getValue();
        return new ResponseEntity<>(value, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete() {
        valueOperations.deleted();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

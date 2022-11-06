package com.example.springrediscache.controllers;

import com.example.springrediscache.entity.Customer;
import com.example.springrediscache.service.RedisHashOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/hash-operations")
public class HashOperationsControllers {
    private final RedisHashOperation redisHashOperation;

    public HashOperationsControllers(RedisHashOperation redisHashOperation) {
        this.redisHashOperation = redisHashOperation;
    }

    @PostMapping("/create")
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        redisHashOperation.create(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Customer> update(@RequestBody Customer customer) {
        redisHashOperation.update(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Map<Integer, Customer>> getAll() {
        Map<Integer, Customer> all = redisHashOperation.getAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        redisHashOperation.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

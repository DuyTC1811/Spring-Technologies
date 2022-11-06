package com.example.springrediscache.controllers;

import com.example.springrediscache.entity.Customer;
import com.example.springrediscache.entity.Range;
import com.example.springrediscache.service.RedisListOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("list-operations")
public class RedisListOperationsControllers {
    private final RedisListOperation redisListOperation;

    public RedisListOperationsControllers(RedisListOperation redisListOperation) {
        this.redisListOperation = redisListOperation;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Customer>> getList(@RequestParam int to, @RequestParam int from) {
        Range range = Range.builder().to(to).from(from).build();
        return new ResponseEntity<>(redisListOperation.listCustomer(range), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<List<Customer>> create(@RequestBody List<Customer> customer) {
        redisListOperation.create(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-id")
    public ResponseEntity<Customer> getInfo(@RequestParam Long index) {
        return new ResponseEntity<>(redisListOperation.getId(index), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{count}")
    public ResponseEntity<Void> delete(@RequestBody Customer customer, @PathVariable Integer count) {
        redisListOperation.delete(customer, count);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

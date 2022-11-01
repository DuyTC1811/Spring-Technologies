package com.example.springjdbctemplate.controllers;

import com.example.springjdbctemplate.entitys.Customer;
import com.example.springjdbctemplate.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(name = "/api")
public class CustomerControllers {
    private final CustomerService customerService;

    public CustomerControllers(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create-customer")
    public ResponseEntity<Customer> getList(@RequestBody Customer customer) {
        customerService.save(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-customer")
    public ResponseEntity<Customer> update(@RequestBody Customer customer) {
        customerService.update(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/info-customer/{customerId}")
    public ResponseEntity<Customer> getInfo(@PathVariable Integer customerId) {
        Customer response = customerService.getById(customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list-customer")
    public ResponseEntity<Map<String, Object>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Map<String, Object> response = customerService.getPage(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-customer/{customerId}")
    public ResponseEntity<Customer> delete(@PathVariable Integer customerId) {
        customerService.delete(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

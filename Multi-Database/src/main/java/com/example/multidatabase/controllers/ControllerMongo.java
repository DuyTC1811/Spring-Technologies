package com.example.multidatabase.controllers;

import com.example.multidatabase.entity.CustomersMongo;
import com.example.multidatabase.services.ICustomerMongoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mongo")
public class ControllerMongo {
    private final ICustomerMongoService customerMongoService;

    public ControllerMongo(ICustomerMongoService customerMongoService) {
        this.customerMongoService = customerMongoService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustomersMongo> create(@RequestBody CustomersMongo customer) {
        CustomersMongo response = customerMongoService.createCustomer(customer);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/info")
    public ResponseEntity<CustomersMongo> getName(String name) {
        CustomersMongo response = customerMongoService.infoCustomer(name);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteByName(Integer id) {
        customerMongoService.deleteByIdCustomer(id);
        return ResponseEntity.ok().body("Successful");
    }
}

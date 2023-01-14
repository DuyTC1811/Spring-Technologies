package com.example.multidatabase.controllers;


import com.example.multidatabase.entity.CustomerPostgres;
import com.example.multidatabase.services.ICustomerPostgresService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/postgres")
public class ControllerPostgres {
    private final ICustomerPostgresService customerPostgresService;

    public ControllerPostgres(ICustomerPostgresService customerPostgresService) {
        this.customerPostgresService = customerPostgresService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerPostgres> created(@RequestBody CustomerPostgres customer) {
        CustomerPostgres response = customerPostgresService.createCustomer(customer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<CustomerPostgres> info(@PathVariable Integer id) {
        CustomerPostgres response = customerPostgresService.infoCustomer(id);
        return ResponseEntity.ok().body(new CustomerPostgres());
    }

    @DeleteMapping("/delete-id/{id}")
    public ResponseEntity<String> deleteId(@PathVariable Integer id) {
        customerPostgresService.deleteByIdCustomer(id);
        return ResponseEntity.ok().body("Successful");
    }


}

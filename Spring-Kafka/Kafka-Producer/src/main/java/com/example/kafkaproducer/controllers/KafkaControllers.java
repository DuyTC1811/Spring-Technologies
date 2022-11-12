package com.example.kafkaproducer.controllers;

import com.example.kafkaproducer.entity.Customer;
import com.example.kafkaproducer.product.KafkaProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class KafkaControllers {
    private final KafkaProducer kafkaProducer;

    public KafkaControllers(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/push")
    public ResponseEntity<Customer> push(@RequestBody Customer customer) {
        kafkaProducer.push(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

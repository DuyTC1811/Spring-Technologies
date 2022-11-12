package com.example.springbatch.job;

import com.example.springbatch.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        return customer;
//        if (customer.getCountry().equals("United States")) return customer; // sử lý lọc những bản ghi United States
//        return null;
    }
}

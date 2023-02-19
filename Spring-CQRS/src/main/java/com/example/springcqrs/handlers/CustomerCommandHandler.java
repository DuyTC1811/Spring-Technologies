package com.example.springcqrs.handlers;

import com.example.springcqrs.cqrs.command.ICommandHandler;
import com.example.springcqrs.dto.CCustomerRequest;
import com.example.springcqrs.dto.CCustomerResponse;
import com.example.springcqrs.entitys.Customer;
import com.example.springcqrs.repositorys.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class CustomerCommandHandler implements ICommandHandler<CCustomerResponse, CCustomerRequest> {
    private final ICustomerRepository customerRepository;

    @Override
    public CCustomerResponse handler(CCustomerRequest command) {
        Customer customer = Customer.create(command);
        Customer save = customerRepository.save(customer);
        return CCustomerResponse.build(save);
    }
}

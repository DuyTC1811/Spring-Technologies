package com.example.springcqrs.handlers;

import com.example.springcqrs.cqrs.model.PageResponse;
import com.example.springcqrs.cqrs.query.IPageHandler;
import com.example.springcqrs.dto.PageRequests;
import com.example.springcqrs.dto.PageResponses;
import com.example.springcqrs.entitys.Customer;
import com.example.springcqrs.enums.CodeError;
import com.example.springcqrs.repositorys.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class TestPageHandler implements IPageHandler<PageResponses, PageRequests> {
    private final ICustomerRepository repository;

    @Override
    public PageResponse<PageResponses> handle(PageRequests query) {
        PageRequest pageRequest = PageRequest.of(1, 3);
        Page<Customer> byNames = repository.findAllByAge(pageRequest, 30);
        return new PageResponse<>(null, CodeError.SUCCESS);
    }
}

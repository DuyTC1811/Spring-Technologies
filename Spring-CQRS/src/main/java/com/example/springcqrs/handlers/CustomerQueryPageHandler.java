package com.example.springcqrs.handlers;

import com.example.springcqrs.cqrs.model.BaseResponse;
import com.example.springcqrs.cqrs.query.IQueryHandler;
import com.example.springcqrs.dto.QCustomerPageRequest;
import com.example.springcqrs.dto.QCustomerPageResponse;
import com.example.springcqrs.entitys.Customer;
import com.example.springcqrs.enums.CodeError;
import com.example.springcqrs.repositorys.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class CustomerQueryPageHandler implements IQueryHandler<QCustomerPageResponse, QCustomerPageRequest> {
    private final ICustomerRepository repository;

    @Override
    public BaseResponse<QCustomerPageResponse> handle(QCustomerPageRequest query) {
        Customer customer = repository.findByName(query.getName()).orElse(null);
        return new BaseResponse<>(null, CodeError.SUCCESS);
    }
}

package com.example.springcqrs.handlers;

import com.example.springcqrs.cqrs.model.BaseResponse;
import com.example.springcqrs.cqrs.query.IQueryHandler;
import com.example.springcqrs.dto.QCustomerRequest;
import com.example.springcqrs.dto.QCustomerResponse;
import com.example.springcqrs.entitys.Customer;
import com.example.springcqrs.repositorys.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.example.springcqrs.enums.CodeError.SUCCESS;

@Component
@Transactional
@RequiredArgsConstructor
public class CustomerQueryHandler implements IQueryHandler<QCustomerResponse, QCustomerRequest> {
    private final ICustomerRepository customerRepository;

    @Override
    public BaseResponse<QCustomerResponse> handle(QCustomerRequest query) {
        int id = query.getId();
        Customer byId = customerRepository.findById(id).get();
        return new BaseResponse<>(null, SUCCESS);
    }
}

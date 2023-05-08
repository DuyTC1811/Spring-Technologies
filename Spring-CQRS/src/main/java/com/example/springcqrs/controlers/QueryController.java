package com.example.springcqrs.controlers;

import com.example.springcqrs.cqrs.controller.ControllerQuery;
import com.example.springcqrs.cqrs.model.BaseResponse;
import com.example.springcqrs.dto.QCustomerRequest;
import com.example.springcqrs.dto.QCustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QueryController extends ControllerQuery<QCustomerResponse, QCustomerRequest> {
    @Override
    @GetMapping("/query")
    protected ResponseEntity<BaseResponse<QCustomerResponse>> executesQuery(@RequestBody QCustomerRequest request) {
        return execute(request);
    }
}

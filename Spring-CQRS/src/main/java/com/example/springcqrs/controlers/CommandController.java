package com.example.springcqrs.controlers;

import com.example.springcqrs.cqrs.controller.ControllerCommand;
import com.example.springcqrs.dto.CCustomerRequest;
import com.example.springcqrs.dto.CCustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommandController extends ControllerCommand<CCustomerResponse, CCustomerRequest> {
    @Override
    @PostMapping("/command")
    protected ResponseEntity<CCustomerResponse> executes(@RequestBody CCustomerRequest request) {
        return execute(request);
    }


}

package com.example.springcqrs.cqrs.controller;


import com.example.springcqrs.cqrs.dispascher.ISpringBus;
import com.example.springcqrs.cqrs.model.BaseResponse;
import com.example.springcqrs.cqrs.query.IQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class ControllerQuery<RESPONSE, REQUEST extends IQuery<RESPONSE>> {
    @Autowired
    private ISpringBus springBus;

    public ControllerQuery() {
    }

    public ResponseEntity<BaseResponse<RESPONSE>> execute(REQUEST request) {
        return new ResponseEntity<>(springBus.executeQuery(request), HttpStatus.OK);
    }

    protected abstract ResponseEntity<BaseResponse<RESPONSE>> executesQuery(REQUEST request);
}

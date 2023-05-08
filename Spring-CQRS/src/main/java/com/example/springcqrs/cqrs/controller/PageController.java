package com.example.springcqrs.cqrs.controller;


import com.example.springcqrs.cqrs.dispascher.ISpringBus;
import com.example.springcqrs.cqrs.model.BaseResponse;
import com.example.springcqrs.cqrs.model.PageResponse;
import com.example.springcqrs.cqrs.query.IPage;
import com.example.springcqrs.cqrs.query.IQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class PageController<RESPONSE, REQUEST extends IPage<RESPONSE>> {
    @Autowired
    private ISpringBus springBus;

    protected PageController() {
    }

    public ResponseEntity<PageResponse<RESPONSE>> execute(REQUEST request) {
        return new ResponseEntity<>(springBus.executePage(request), HttpStatus.OK);
    }

    protected abstract ResponseEntity<PageResponse<RESPONSE>> executesPage(REQUEST request);
}

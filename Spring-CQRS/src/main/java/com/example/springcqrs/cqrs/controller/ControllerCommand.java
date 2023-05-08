package com.example.springcqrs.cqrs.controller;


import com.example.springcqrs.cqrs.command.ICommand;
import com.example.springcqrs.cqrs.dispascher.ISpringBus;
import com.example.springcqrs.cqrs.model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class ControllerCommand<RESPONSE, REQUEST extends ICommand<RESPONSE>> {
    @Autowired
    private ISpringBus springBus;

    protected ControllerCommand() {
    }

    public ResponseEntity<BaseResponse<RESPONSE>> execute(REQUEST request) {
        return new ResponseEntity<>(springBus.executeCommand(request), HttpStatus.OK);
    }

    protected abstract ResponseEntity<BaseResponse<RESPONSE>> executesCommand(REQUEST request);
}

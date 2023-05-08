package com.example.springcqrs.cqrs.dispascher;


import com.example.springcqrs.cqrs.command.ICommand;
import com.example.springcqrs.cqrs.model.BaseResponse;
import com.example.springcqrs.cqrs.model.PageResponse;
import com.example.springcqrs.cqrs.query.IPage;
import com.example.springcqrs.cqrs.query.IQuery;

public interface ISpringBus {
    <RESPONSE, REQUEST extends ICommand<RESPONSE>> BaseResponse<RESPONSE> executeCommand(REQUEST command);

    <RESPONSE, REQUEST extends IQuery<RESPONSE>> BaseResponse<RESPONSE> executeQuery(REQUEST query);

    <RESPONSE, REQUEST extends IPage<RESPONSE>> PageResponse<RESPONSE> executePage(REQUEST pageResponse);
}

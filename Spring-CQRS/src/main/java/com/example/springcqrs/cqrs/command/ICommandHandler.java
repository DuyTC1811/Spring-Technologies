package com.example.springcqrs.cqrs.command;

import com.example.springcqrs.cqrs.model.BaseResponse;

public interface ICommandHandler<RESPONSE, REQUEST extends ICommand<RESPONSE>> {
    BaseResponse<RESPONSE> handler(REQUEST command);
}

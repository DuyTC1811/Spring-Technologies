package com.example.springcqrs.cqrs.command;

public interface ICommandHandler<RESPONSE, REQUEST extends ICommand<RESPONSE>> {
    RESPONSE handler(REQUEST command);
}

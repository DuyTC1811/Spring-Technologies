package com.example.springcqrs.cqrs.dispascher;


import com.example.springcqrs.cqrs.command.ICommand;
import com.example.springcqrs.cqrs.query.IQuery;

public interface ISpringBus {
    <RESPONSE, REQUEST extends ICommand<RESPONSE>> RESPONSE executeCommand(REQUEST command);

    <RESPONSE, REQUEST extends IQuery<RESPONSE>> RESPONSE executeQuery(REQUEST query);
}

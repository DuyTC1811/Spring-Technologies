package com.example.springcqrs.cqrs.dispascher;


import com.example.springcqrs.cqrs.command.ICommand;
import com.example.springcqrs.cqrs.command.ICommandHandler;
import com.example.springcqrs.cqrs.query.IQuery;
import com.example.springcqrs.cqrs.query.IQueryHandler;
import org.springframework.stereotype.Component;

@Component
public class SpringBusImpl implements ISpringBus {

    private final Registry registry;

    public SpringBusImpl(Registry registry) {
        this.registry = registry;
    }

    @Override
    public <RESPONSE, REQUEST extends ICommand<RESPONSE>> RESPONSE executeCommand(REQUEST command) {
        ICommandHandler<RESPONSE, REQUEST> commandHandler = registry.getCmd(command.getClass());
        return commandHandler.handler(command);
    }

    @Override
    public <RESPONSE, REQUEST extends IQuery<RESPONSE>> RESPONSE executeQuery(REQUEST query) {
        IQueryHandler<RESPONSE, REQUEST> queryHandler = registry.getQuery(query.getClass());
        return queryHandler.handle(query);
    }
}

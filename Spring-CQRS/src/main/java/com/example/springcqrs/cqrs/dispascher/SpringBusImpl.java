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
        ICommandHandler<RESPONSE, REQUEST> ICommandHandler = (ICommandHandler<RESPONSE, REQUEST>) registry.getCmd(command.getClass());
        return ICommandHandler.handler(command);
    }

    @Override
    public <RESPONSE, REQUEST extends IQuery<RESPONSE>> RESPONSE executeQuery(REQUEST query) {
        IQueryHandler<RESPONSE, REQUEST> IQueryHandler = (IQueryHandler<RESPONSE, REQUEST>) registry.getQuery(query.getClass());
        return IQueryHandler.handle(query);
    }
}

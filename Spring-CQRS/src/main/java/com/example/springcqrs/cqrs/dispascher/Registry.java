package com.example.springcqrs.cqrs.dispascher;


import com.example.springcqrs.cqrs.command.CommandProvider;
import com.example.springcqrs.cqrs.command.ICommand;
import com.example.springcqrs.cqrs.command.ICommandHandler;
import com.example.springcqrs.cqrs.query.*;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public final class Registry {

    private final Map<Class<? extends ICommand>, CommandProvider> commandProviderMap = new HashMap<>();
    private final Map<Class<? extends IQuery>, QueryProvider> queryProviderMap = new HashMap<>();
    private final Map<Class<? extends IPage>, PageProvider> qageProviderMap = new HashMap<>();

    public Registry(ApplicationContext applicationContext) {
        String[] names = applicationContext.getBeanNamesForType(ICommandHandler.class);
        for (String name : names) {
            registerCommand(applicationContext, name);
        }
        names = applicationContext.getBeanNamesForType(IQueryHandler.class);
        for (String name : names) {
            registerQuery(applicationContext, name);
        }
        names = applicationContext.getBeanNamesForType(IPageHandler.class);
        for (String name : names) {
            registerPage(applicationContext, name);
        }
    }

    @SuppressWarnings("unchecked")
    private void registerCommand(ApplicationContext applicationContext, String name) {
        Class<ICommandHandler<?, ?>> handlerClass = (Class<ICommandHandler<?, ?>>) applicationContext.getType(name);
        Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(handlerClass, ICommandHandler.class);
        Class<? extends ICommand> commandType = (Class<? extends ICommand>) generics[1];
        commandProviderMap.put(commandType, new CommandProvider(applicationContext, handlerClass));
    }

    @SuppressWarnings({"unchecked"})
    private void registerQuery(ApplicationContext applicationContext, String name) {
        Class<IQueryHandler<?, ?>> handlerClass = (Class<IQueryHandler<?, ?>>) applicationContext.getType(name);
        Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(handlerClass, IQueryHandler.class);
        Class<? extends IQuery> queryType = (Class<? extends IQuery>) generics[1];
        queryProviderMap.put(queryType, new QueryProvider(applicationContext, handlerClass));
    }

    @SuppressWarnings({"unchecked"})
    private void registerPage(ApplicationContext applicationContext, String name) {
        Class<IPageHandler<?, ?>> handlerClass = (Class<IPageHandler<?, ?>>) applicationContext.getType(name);
        Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(handlerClass, IPageHandler.class);
        Class<? extends IPage> pageType = (Class<? extends IPage>) generics[1];
        qageProviderMap.put(pageType, new PageProvider(applicationContext, handlerClass));
    }

    @SuppressWarnings("unchecked")
    <RESPONSE, REQUEST extends ICommand<RESPONSE>> ICommandHandler<RESPONSE, REQUEST> getCmd(Class<REQUEST> commandClass) {
        return commandProviderMap.get(commandClass).get();
    }

    @SuppressWarnings("unchecked")
    <RESPONSE, REQUEST extends IQuery<RESPONSE>> IQueryHandler<RESPONSE, REQUEST> getQuery(Class<REQUEST> commandClass) {
        return queryProviderMap.get(commandClass).get();
    }

    @SuppressWarnings("unchecked")
    <RESPONSE, REQUEST extends IPage<RESPONSE>> IPageHandler<RESPONSE, REQUEST> getPage(Class<REQUEST> commandClass) {
        return qageProviderMap.get(commandClass).get();
    }
}

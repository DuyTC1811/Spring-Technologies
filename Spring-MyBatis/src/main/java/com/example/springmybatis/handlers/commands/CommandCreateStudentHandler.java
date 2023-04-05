package com.example.springmybatis.handlers.commands;

import com.example.springmybatis.dto.requests.CreateStudentRequest;
import com.example.springmybatis.dto.responses.CreateStudentResponse;
import com.example.springmybatis.mapper.commands.ICreateStudentMapper;
import io.cqrs.command.ICommandHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CommandCreateStudentHandler implements ICommandHandler<CreateStudentResponse, CreateStudentRequest> {
    private final ICreateStudentMapper createStudentMapper;

    public CommandCreateStudentHandler(ICreateStudentMapper createStudentMapper) {
        this.createStudentMapper = createStudentMapper;
    }

    @Override
    @Transactional
    public CreateStudentResponse handler(CreateStudentRequest request) {
        request.setId(UUID.randomUUID().toString());
        createStudentMapper.insertStudent(request);
        return new CreateStudentResponse("Create success...!");
    }
}

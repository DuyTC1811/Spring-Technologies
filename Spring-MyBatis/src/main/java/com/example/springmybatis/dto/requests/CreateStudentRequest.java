package com.example.springmybatis.dto.requests;

import com.example.springmybatis.dto.responses.CreateStudentResponse;
import io.cqrs.command.ICommand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudentRequest implements ICommand<CreateStudentResponse> {
    private String id;
    private String name;
    private String address;
    private int age;
    private String male;
}

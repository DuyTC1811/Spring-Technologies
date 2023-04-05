package com.example.springmybatis.controllers.commands;

import com.example.springmybatis.dto.requests.CreateStudentRequest;
import com.example.springmybatis.dto.responses.CreateStudentResponse;
import io.cqrs.controller.CommandController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CommandCreateStudentCtrl extends CommandController<CreateStudentResponse, CreateStudentRequest> {

    @Override
    @PostMapping("/insert-student")
    protected ResponseEntity<CreateStudentResponse> coordinator(@RequestBody CreateStudentRequest request) {
        return execute(request);
    }
}

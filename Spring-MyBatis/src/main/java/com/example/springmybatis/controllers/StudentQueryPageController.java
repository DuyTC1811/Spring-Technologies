package com.example.springmybatis.controllers;

import com.example.springmybatis.dto.requests.StudentPageRequest;
import com.example.springmybatis.dto.responses.StudentPageResponse;
import io.cqrs.controller.QueryController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StudentQueryPageController extends QueryController<StudentPageResponse, StudentPageRequest> {
    @Override
    @RequestMapping("/page")
    protected ResponseEntity<StudentPageResponse> coordinator(@RequestBody StudentPageRequest request) {
        return execute(request);
    }
}

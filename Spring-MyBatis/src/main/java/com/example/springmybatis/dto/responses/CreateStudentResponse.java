package com.example.springmybatis.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateStudentResponse {
    private String message;

    public CreateStudentResponse(String message) {
        this.message = message;
    }
}

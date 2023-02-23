package com.example.springmybatis.dto.requests;

import com.example.springmybatis.dto.responses.StudentPageResponse;
import io.cqrs.query.IQuery;
import lombok.Data;

@Data
public class StudentPageRequest implements IQuery<StudentPageResponse> {
    private String id;
    private int limit;
}

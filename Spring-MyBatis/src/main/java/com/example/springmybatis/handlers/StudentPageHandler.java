package com.example.springmybatis.handlers;

import com.example.springmybatis.dto.requests.StudentPageRequest;
import com.example.springmybatis.dto.responses.StudentPageResponse;
import com.example.springmybatis.entity.StudentVO;
import com.example.springmybatis.mapper.IStudentMapper;
import io.cqrs.query.IQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StudentPageHandler implements IQueryHandler<StudentPageResponse, StudentPageRequest> {
    private final IStudentMapper studentMapper;

    @Override
    public StudentPageResponse handler(StudentPageRequest request) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", request.getId());
        param.put("limit", request.getLimit());
        List<StudentVO> listStudent = studentMapper.getPage(param);
        int totalElements = studentMapper.totalElement();

        return StudentPageResponse.builder()
                .totalItems(totalElements)
                .limit(request.getLimit())
                .data(listStudent)
                .build();
    }
}

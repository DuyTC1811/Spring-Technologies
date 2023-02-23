package com.example.springmybatis.dto.responses;

import com.example.springmybatis.entity.StudentVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StudentPageResponse {
    private int totalItems;
    private List<StudentVO> data;
    private int limit;
}

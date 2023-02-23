package com.example.springmybatis.handlers;

import com.example.springmybatis.entity.StudentVO;

import java.util.List;
import java.util.Map;

public interface IStudentService {
    void insert(StudentVO student);
    int update(StudentVO student);
    void delete(String id);
    List<StudentVO> getList();
    StudentVO infor(String id);
}

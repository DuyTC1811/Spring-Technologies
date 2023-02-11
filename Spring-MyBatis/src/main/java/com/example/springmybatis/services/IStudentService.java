package com.example.springmybatis.services;

import com.example.springmybatis.vo.StudentVO;

import java.util.List;
import java.util.Map;

public interface IStudentService {
    int insert(StudentVO student);
    int update(StudentVO student);
    void delete(String id);
    List<StudentVO> getList();
    StudentVO infor(String id);
    Map<String, Object> getPage(String currentId , int offset);
}

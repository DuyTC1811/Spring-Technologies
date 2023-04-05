package com.example.springmybatis.mapper.commands;

import com.example.springmybatis.dto.requests.CreateStudentRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ICreateStudentMapper {
    int insertStudent(CreateStudentRequest request);
}

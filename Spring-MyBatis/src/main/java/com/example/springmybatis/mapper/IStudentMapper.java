package com.example.springmybatis.mapper;

import com.example.springmybatis.entity.StudentVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface IStudentMapper {
    List<StudentVO> selectAllStudent();

    int insertStudent(StudentVO students);

    int updateStudent(StudentVO students);

    StudentVO info(String id);

    List<StudentVO> getPage(Map<String, Object> param);

    void deleteStudentById(String id);

    int totalElement();
}

package com.example.springmybatis.mapper;

import com.example.springmybatis.vo.StudentVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudentMapper {
    List<StudentVO> selectAllStudent();

    int insertStudent(StudentVO students);

    StudentVO updateStudent(StudentVO students);

    StudentVO info(String id);

    List<StudentVO> getPage(int limit, int offset);

    void deleteStudentById(String id);

    int totalElement();
}

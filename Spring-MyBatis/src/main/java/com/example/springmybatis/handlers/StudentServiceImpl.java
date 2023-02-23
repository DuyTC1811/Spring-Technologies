package com.example.springmybatis.handlers;

import com.example.springmybatis.entity.StudentVO;
import com.example.springmybatis.mapper.IStudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StudentServiceImpl implements IStudentService {
    private final IStudentMapper IStudentMapper;

    public StudentServiceImpl(IStudentMapper IStudentMapper) {
        this.IStudentMapper = IStudentMapper;
    }

    @Override
    public void insert(StudentVO student) {
        student.setId(UUID.randomUUID().toString());
        IStudentMapper.insertStudent(student);
    }

    @Override
    public int update(StudentVO student) {
        return IStudentMapper.updateStudent(student);
    }

    @Override
    public void delete(String id) {
        IStudentMapper.deleteStudentById(id);
    }

    @Override
    public List<StudentVO> getList() {
        return IStudentMapper.selectAllStudent();
    }

    @Override
    public StudentVO infor(String id) {
        return IStudentMapper.info(id);
    }

}

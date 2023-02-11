package com.example.springmybatis.services;

import com.example.springmybatis.mapper.StudentMapper;
import com.example.springmybatis.vo.StudentVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class StudentServiceImpl implements IStudentService {
    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public int insert(StudentVO student) {
        student.setId(UUID.randomUUID().toString());
        return studentMapper.insertStudent(student);
    }

    @Override
    public int update(StudentVO student) {
        return studentMapper.updateStudent(student);
    }

    @Override
    public void delete(String id) {
        studentMapper.deleteStudentById(id);
    }

    @Override
    public List<StudentVO> getList() {
        return studentMapper.selectAllStudent();
    }

    @Override
    public StudentVO infor(String id) {
        return studentMapper.info(id);
    }

    @Override
    public Map<String, Object> getPage(String currentId, int limit) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", currentId);
        param.put("limit", limit);

        Map<String, Object> response = new HashMap<>();
        List<StudentVO> listStudent = studentMapper.getPage(param);
        int totalElements = studentMapper.totalElement();
        response.put("students", listStudent);
        response.put("totalItems", totalElements);
        response.put("totalPages", limit);
        return response;
    }
}

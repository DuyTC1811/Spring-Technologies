package com.example.springmybatis.controllers;

import com.example.springmybatis.entity.StudentVO;
import com.example.springmybatis.handlers.IStudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {
    private final IStudentService studentService;

    public MainController(IStudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/insert-student")
    public ResponseEntity<String> insert(@RequestBody StudentVO student) {
        studentService.insert(student);
        return ResponseEntity.ok().body(student.getId());
    }

    @PutMapping("/update-student")
    public ResponseEntity<Integer> update(@RequestBody StudentVO student) {
        int response = studentService.update(student);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> insert(@PathVariable String id) {
        studentService.delete(id);
        return ResponseEntity.ok().body("Successful");
    }

    @GetMapping("/list-student")
    public ResponseEntity<List<StudentVO>> insert() {
        List<StudentVO> response = studentService.getList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/info-student/{id}")
    public ResponseEntity<StudentVO> info(@PathVariable String id) {
        StudentVO response = studentService.infor(id);
        return ResponseEntity.ok().body(response);
    }

}

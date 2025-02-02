package com.uday.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uday.model.Student;

@Service
public interface StudentService {
	public Student create(Student student);
	
	public List<Student> getAllStudents();
	
	public Student getStudentById(Integer id);
	
	public Student update(Student student);
	
	public String deleteStudent(Integer id);

}

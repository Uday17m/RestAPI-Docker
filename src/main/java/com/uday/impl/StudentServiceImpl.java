package com.uday.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uday.model.Student;
import com.uday.repository.StudentRepository;
import com.uday.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	StudentRepository studentRepository;
	
	@Override
	public Student create(Student student) {
		return studentRepository.save(student);
	}

	@Override
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	@Override
	public Student getStudentById(Integer id) {
		return studentRepository.findById(id).get();
	}

	@Override
	public Student update(Student student) {
		return studentRepository.save(student);
	}

	@Override
	public String deleteStudent(Integer id) {
		studentRepository.deleteById(id);
		return "Record Deleted";
	}
	
	
}

package com.uday.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.uday.model.Student;
import com.uday.service.StudentService;



@RestController
@RequestMapping("/students")
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@GetMapping("/")
	public List<Student> getStudents() {
		return studentService.getAllStudents();
		
	}
	
	@PostMapping("/store")
	public Student saveStudent(@RequestBody Student student) {
		return studentService.create(student);	
	}
	
	@GetMapping("/{id}")
	public Student getStudents(@PathVariable Integer id) {
		return studentService.getStudentById(id);
		
	}
	
	@PutMapping("/update")
	public Student updateStudent(@RequestBody Student student) {
		return studentService.update(student);
	}
	 
	@DeleteMapping("/delete/{id}")
	public String deleteStudent(@PathVariable Integer id) {
		return studentService.deleteStudent(id);
		 
	}

}

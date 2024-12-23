package com.uday.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uday.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}

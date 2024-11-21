package com.school.mgnt.sys.student.management.student;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.mgnt.sys.student.management.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}

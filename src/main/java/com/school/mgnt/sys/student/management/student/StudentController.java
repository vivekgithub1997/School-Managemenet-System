package com.school.mgnt.sys.student.management.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentServices studentServices;

	@PostMapping("/addstudent/{schoolId}")
	public ResponseEntity<StudentResponse> addStudent(@RequestBody StudentRequest studentRequest,
			@PathVariable int schoolId) {

		StudentResponse student = studentServices.addStudent(studentRequest, schoolId);

		return new ResponseEntity<StudentResponse>(student, HttpStatus.CREATED);
	}

}

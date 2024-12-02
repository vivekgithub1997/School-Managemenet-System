package com.school.mgnt.sys.student.management.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentServices studentServices;

	@PostMapping("/addstudent/{schoolId}")
	public ResponseEntity<StudentResponse> addStudent(@RequestBody StudentRequest studentRequest,
			@PathVariable int schoolId,
			@Parameter(description = "Study class ", required = true, schema = @Schema(allowableValues = { "1", "2",
					"3", "4", "5", "6", "7", "8", "9" })) @PathParam(value = "studyClass") String studyClass) {

		StudentResponse student = studentServices.addStudent(studentRequest, schoolId, studyClass);

		return new ResponseEntity<StudentResponse>(student, HttpStatus.CREATED);
	}

}

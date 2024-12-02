package com.school.mgnt.sys.school.management;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.school.mgnt.sys.student.management.student.StudentResponse;
import com.school.mgnt.sys.student.management.student.StudentServices;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/school")
public class SchoolController {

	@Autowired
	private SchoolServices schoolServices;

	@Autowired
	private StudentServices studentServices;

	@GetMapping("/health-check")
	public String Testing() {
		return "Server running :  port no. 9090";
	}

	@PostMapping("/add/new-school/{schoolType}")
	public ResponseEntity<SchoolResponse> RegisterNewSchool(@RequestBody SchoolRequest schoolRequest,
			@Parameter(description = "school type ", required = true, schema = @Schema(allowableValues = {
					"1-5 PRIMARY", "6-9 MIDDLE" })) @PathVariable String schoolType) {

		SchoolResponse newSchool = schoolServices.addNewSchool(schoolRequest, schoolType);

		return new ResponseEntity<SchoolResponse>(newSchool, HttpStatus.CREATED);
	}

	@GetMapping("/students")
	public ResponseEntity<List<StudentResponse>> getAllStudentsBasedOnSchoolCode(@RequestParam String schoolcode) {

		List<StudentResponse> allStudentBasedOnSchoolCode = studentServices.getAllStudentBasedOnSchoolCode(schoolcode);

		return new ResponseEntity<List<StudentResponse>>(allStudentBasedOnSchoolCode, HttpStatus.OK);
	}
}

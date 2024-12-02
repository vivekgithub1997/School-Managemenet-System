package com.school.mgnt.sys.teacher.management.teacher;

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
@RequestMapping("/teacher")
public class TeacherController {

	@Autowired
	private TeacherServices teacherServices;

	@PostMapping("/add/{schoolId}")
	public ResponseEntity<TeacherResponse> addTeacher(@RequestBody TeacherRequest teacherRequest,
			@PathVariable int schoolId,
			@Parameter(description = "Qualification ", required = true, schema = @Schema(allowableValues = {
					"IITIAN:IITIAN", "MTECH:PG", "B.ed:B.ed", "BTECH:UG",
					"OTHER:12th" })) @PathParam(value = "Qualification") String qualification) {

		TeacherResponse response = teacherServices.addTeacher(teacherRequest, schoolId, qualification);

		return new ResponseEntity<TeacherResponse>(response, HttpStatus.CREATED);

	}
}

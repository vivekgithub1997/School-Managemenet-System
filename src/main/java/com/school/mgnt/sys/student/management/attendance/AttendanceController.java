package com.school.mgnt.sys.student.management.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceServices attendanceServices;

	@PostMapping("mark-attendace/{teacherId}/{registrationId}/{subjectName}")
	public ResponseEntity<AttendanceResponse> markAttendance(@PathVariable int teacherId,
			@PathVariable long registrationId,
			@Parameter(description = "Subject Name", required = true, schema = @Schema(allowableValues = { "HINDI",
					"COMPUTER", "ENGLISH", "ARTS", "SCIENCE", "MATH",
					"SCIENCE-EXPERIMENTS" })) @PathParam(value = "subjectName") String subjectName) {

		AttendanceResponse markAttendance = attendanceServices.markAttendance(teacherId, registrationId, subjectName);

		return new ResponseEntity<AttendanceResponse>(markAttendance, HttpStatus.CREATED);

	}

}

package com.school.mgnt.sys.student.management.attendance;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AttendanceResponse {
	private String messages;
	private int attendanceId;

	private String studentName; 
	private long RegistrationId;

	private String studyClass;
	private String subjectName;
	private LocalDate date;

	private String attendanceStatus;
	private String shiftTime;
	private String remarks;

	private String teacherName; 
	private int teacherId;

	// Getters and Setters
}

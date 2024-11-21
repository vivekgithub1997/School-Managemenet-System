package com.school.mgnt.sys.student.management.student;

import lombok.Data;

@Data
public class StudentResponse {

	private String message;

	private int studentId;

	private int schoolId;

	private String schoolName;

	private String studentFirstName;

	private String studentLastName;

	private String dateOfBirth;

	private String gender;

	private String studentAddress;

	private long emergencyNumber;

	private long mobileNumber;

	private String parentEmail;

}

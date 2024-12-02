package com.school.mgnt.sys.student.management.student;

import java.time.LocalDate;

import lombok.Data;

@Data
public class StudentResponse {

	private String message;

	private long registrationId;

	private int schoolId;

	private String schoolName;

	private String schoolCode;

	private String schoolType;

	private String schoolTiming;

	private String studentFirstName;

	private String studentLastName;

	private String dateOfBirth;

	private int age;

	private String studyClass;

	private String gender;

	private String studentAddress;

	private LocalDate registrationDate;

	private long mobileNumber;

	private String mobileNumberRelation;

	private String studentEmail;

}

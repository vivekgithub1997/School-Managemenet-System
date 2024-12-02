package com.school.mgnt.sys.student.management.student;

import lombok.Data;

@Data
public class StudentRequest {

	private String studentFirstName;

	private String studentLastName;

	private String dateOfBirth;

	private String gender;

	private String studentAddress;

	private long mobileNumber;

	private String mobileNumberRelation;

}

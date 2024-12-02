package com.school.mgnt.sys.teacher.management.teacher;

import java.util.List;

import lombok.Data;

@Data
public class TeacherRequest {
	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNumber;
	private String dob;

	private List<String> subjectSpecializations;

}

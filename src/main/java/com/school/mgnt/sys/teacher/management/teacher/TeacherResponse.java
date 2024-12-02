package com.school.mgnt.sys.teacher.management.teacher;

import java.util.List;

import lombok.Data;

@Data
public class TeacherResponse {

	private String message;
	private int teacherId;
	private int schoolId;
	private String schoolCode;
	private String schoolName;
	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNumber;
	private String dateOfJoining;
	private String qualification;
	private String qualificationType;
	private String dob;
	private double salary;
	private String availableStatus;
	private String currentStaus;

	private String teacherType;

	private List<String> subjectSpecializations;

}

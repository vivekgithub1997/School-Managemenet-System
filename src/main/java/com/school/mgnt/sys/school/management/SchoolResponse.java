package com.school.mgnt.sys.school.management;

import lombok.Data;

@Data
public class SchoolResponse {
	private String message;

	private int schoolId;

	private String schoolName;

	private long mobileNumber;

	private String emailId;

	private String schoolAddress;

	private String numberOfClass;

	private String schoolsHours;

	private String holidays;

}

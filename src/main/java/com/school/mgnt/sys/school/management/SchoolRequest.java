package com.school.mgnt.sys.school.management;

import lombok.Data;

@Data
public class SchoolRequest {
	
	private String schoolName;

	private long mobileNumber;

	private String emailId;

	private String schoolAddress;

	private String numberOfClass;

	private String schoolsHours;

	private String holidays;

}

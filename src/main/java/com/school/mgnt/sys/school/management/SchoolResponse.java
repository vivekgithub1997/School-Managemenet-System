package com.school.mgnt.sys.school.management;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SchoolResponse {
	private String message;

	private int schoolId;

	private String schoolCode;

	private String schoolName;

	private long mobileNumber;

	private String emailId;

	private String schoolAddress;

	private String schoolType;

	private String schoolTiming;

	private String holidays;

	private LocalDate createdDate;

}

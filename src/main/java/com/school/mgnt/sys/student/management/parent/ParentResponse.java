package com.school.mgnt.sys.student.management.parent;

import lombok.Data;

@Data
public class ParentResponse {
	private String message;
	private long registrationId;
	private int parentId;
	private String parentFirstName;
	private String parentLastName;
	private String motherName;
	private String parentOccupation;

	private String parentEmailId;

	private String mobileNumber;

	private String parentAddress;

}

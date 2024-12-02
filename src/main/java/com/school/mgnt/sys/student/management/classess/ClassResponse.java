package com.school.mgnt.sys.student.management.classess;

import lombok.Data;

@Data
public class ClassResponse {

	private String message;

	private int classId;

	private int schoolId;

	private String schoolCode;

	private String schoolName;

	private String className;

	private String classType;

	private int classRoomNo;

	private int studentCapacity;

	private String resources;

}

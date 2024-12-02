package com.school.mgnt.sys.student.management.classess;

import lombok.Data;

@Data
public class ClassRequest {

	private String className;

	private String classType;

	private int classRoomNo;

	private int studentCapacity;

	private String resources;

}

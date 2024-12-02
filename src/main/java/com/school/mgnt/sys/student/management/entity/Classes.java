package com.school.mgnt.sys.student.management.entity;

import java.util.List;

import com.school.mgnt.sys.school.management.School;
import com.school.mgnt.sys.teacher.management.teacher.Teacher;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Classes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int classId;

	@ManyToOne
	@JoinColumn(name = "school_id", nullable = false)
	private School school;

	private String schoolCode;

	private String schoolName;

	private String className;

	private String classType;

	private int classRoomNo;

	private int studentCapacity;

	private String resources;

	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	@OneToMany(mappedBy = "classes", cascade = CascadeType.ALL)
	private List<Grade> grades;

}

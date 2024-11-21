package com.school.mgnt.sys.school.management;

import java.util.List;

import com.school.mgnt.sys.student.management.entity.Classes;
import com.school.mgnt.sys.student.management.entity.Student;
import com.school.mgnt.sys.teacher.management.Teacher;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "school")
@Data
public class School {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int schoolId;

	private String schoolName;

	private long mobileNumber;

	private String emailId;

	private String schoolAddress;

	private String numberOfClass;

	private String schoolsHours;

	private String holidays;

	@OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
	private List<Student> student;

	@OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
	private List<Classes> classes;

	@OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
	private List<Teacher> teachers;

}

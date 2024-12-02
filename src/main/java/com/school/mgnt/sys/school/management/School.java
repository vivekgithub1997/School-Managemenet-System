package com.school.mgnt.sys.school.management;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.school.mgnt.sys.student.management.entity.Classes;
import com.school.mgnt.sys.student.management.entity.Student;
import com.school.mgnt.sys.teacher.management.teacher.Teacher;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity

@Data
public class School {

	@Id
	private int schoolId;

	@Column(nullable = false, unique = true)
	private String schoolCode;

	private String schoolName;

	private long mobileNumber;

	private String emailId;

	private String schoolAddress;

	private String schoolType;

	private String schoolTiming;

	private String holidays;

	private LocalDate createdDate;

	@OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Student> student;

	@OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
	private List<Classes> classes;

	@OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
	private List<Teacher> teachers;

}

package com.school.mgnt.sys.student.management.entity;

import java.util.List;

import com.school.mgnt.sys.school.management.School;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "students")
@Data
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int studentId;

	private String studentFirstName;

	private String studentLastName;

	private String dateOfBirth;

	private String gender;

	private String studentAddress;

	private long emergencyNumber;

	private long mobileNumber;

	private String parentEmail;

	@ManyToOne
	@JoinColumn(name = "school_id", nullable = false)
	private School school;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<Attendance> attendances;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<Grade> grades;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<Fee> fees;

	@OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
	private Parent parent;

}

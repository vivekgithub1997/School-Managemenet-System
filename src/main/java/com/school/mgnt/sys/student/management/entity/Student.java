package com.school.mgnt.sys.student.management.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.school.mgnt.sys.school.management.School;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Student {

	@Id
	private long registrationId;

	private String studentFirstName;

	private String studentLastName;

	private String dateOfBirth;

	private int age;

	private String studyClass;

	private String gender;

	private String studentAddress;

	private long mobileNumber;

	private String mobileNumberRelation;

	private String schoolCode;

	private String schoolType;

	private String schoolTiming;

	@Email
	@NotBlank
	@Column(unique = true)
	private String studentEmailId;

	private LocalDate registrationDate;

	private String pdfGenerated = "NOT GENERATED";

	@ManyToOne
	@JoinColumn(name = "school_id", nullable = false)
	@JsonIgnore
	private School school;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Attendance> attendances;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<Grade> grades;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<Fee> fees;

	@OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
	private Parent parent;

	@OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
	private AdmissionReceipt admissionReceipt;

}

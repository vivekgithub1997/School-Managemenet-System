package com.school.mgnt.sys.student.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data

public class Parent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int parentId;

	@OneToOne
	@JoinColumn(name = "registration_id", nullable = false)
	private Student student;

	private String parentFirstName;
	private String parentLastName;
	private String motherName;
	private String parentOccupation;

	@Email
	@NotBlank
	@Column(unique = true)
	private String parentEmailId;

	private String mobileNumber;

	private String parentAddress;

}

package com.school.mgnt.sys.student.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "parents")
public class Parent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int parentId;

	@OneToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNumber;

}

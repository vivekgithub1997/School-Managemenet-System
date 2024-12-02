package com.school.mgnt.sys.student.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Grade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int gradeId;

	@ManyToOne
	@JoinColumn(name = "registration_id", nullable = false)
	private Student student;

	@ManyToOne
	@JoinColumn(name = "classes_id", nullable = false)
	private Classes classes;

	private String subject;
	private String grade;
	private String term;

}

package com.school.mgnt.sys.teacher.management.teacher;

import java.util.List;

import com.school.mgnt.sys.school.management.School;
import com.school.mgnt.sys.student.management.entity.Classes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "teacher")
public class Teacher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int teacherId;

	@ManyToOne
	@JoinColumn(name = "school_id", nullable = false)
	private School school;

	private String schoolCode;
	private String schoolName;
	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNumber;
	private String dateOfJoining;
	private String qualification;
	private String qualificationType;
	private String dob;
	private double salary;
	private String availableStatus;
	private String currentStaus;

	private String teacherType;

	@ElementCollection
	@CollectionTable(name = "teacher_subjects", joinColumns = @JoinColumn(name = "teacher_id"))
	@Column(name = "subject_specialization")
	private List<String> subjectSpecializations;

	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Classes> classes;

}

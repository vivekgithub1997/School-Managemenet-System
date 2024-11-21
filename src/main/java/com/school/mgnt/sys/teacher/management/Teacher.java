package com.school.mgnt.sys.teacher.management;

import java.util.List;

import com.school.mgnt.sys.school.management.School;
import com.school.mgnt.sys.student.management.entity.Classes;

import jakarta.persistence.CascadeType;
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

	private String firstName;
	private String lastName;
	private String email;
	private String mobileNumber;

	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	private List<Classes> classes;

}

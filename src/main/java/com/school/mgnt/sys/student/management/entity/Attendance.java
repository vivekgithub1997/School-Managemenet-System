package com.school.mgnt.sys.student.management.entity;

import java.time.LocalDate;

import com.school.mgnt.sys.teacher.management.teacher.Teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Attendance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int attendanceId;

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	private String studyClass;

	private String subjectName;

	private LocalDate date;

	private String attendanceStatus;

	private String shiftTime;

	private String remarks;

	private String teacherName;

	@ManyToOne
	@JoinColumn(name = "marked_by", nullable = false)
	private Teacher teacher;

}

package com.school.mgnt.sys.student.management.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AdmissionReceipt {
	@Id
	private String receiptId;
	private String studentName;
	private String studentEmailId;
	private String studyClass;
	private String schoolName;
	private String schoolCode;
	private String schoolTiming;
	private long schoolMobile;
	private String schoolEmail;
	private String schoolAddress;
	private String admissionDate;
	private String dob;
	private String gender;
	private double admissionFee;
	private double monthlyFee;

	@OneToOne
	@JoinColumn(name = "registration_Id")
	private Student student;

}

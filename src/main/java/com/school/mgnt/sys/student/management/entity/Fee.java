package com.school.mgnt.sys.student.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "fees")
public class Fee {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int feeId;

    @ManyToOne
    @JoinColumn(name = "registration_id", nullable = false)
    private Student student;

    private double amountDue;
    private double amountPaid;
    private String dueDate;
    private String paymentDate;

}

package com.school.mgnt.sys.student.management.student;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.mgnt.sys.student.management.entity.AdmissionReceipt;

public interface AdmissionRepository extends JpaRepository<AdmissionReceipt, String> {

}

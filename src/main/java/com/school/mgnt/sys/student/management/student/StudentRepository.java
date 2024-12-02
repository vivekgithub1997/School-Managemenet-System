package com.school.mgnt.sys.student.management.student;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.school.mgnt.sys.student.management.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query("SELECT s FROM Student s WHERE s.registrationId BETWEEN :startId AND :endId ORDER BY s.registrationId DESC")
	Page<Student> findTopByRegistrationIdInRange(@Param("startId") Long startId, @Param("endId") Long endId,
			Pageable pageable);

	List<Student> findByStudentFirstNameAndStudentLastNameAndDateOfBirthAndMobileNumberAndSchool_SchoolId(
			String firstName, String lastName, String dob, long mobileNumber, int schoolId);

	List<Student> findBySchoolCode(String schoolCode);

	boolean existsByStudentEmailId(String studentemailId);

	Optional<Student> findByRegistrationIdAndSchool_SchoolId(long registrationId, int schoolId);

}

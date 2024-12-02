package com.school.mgnt.sys.student.management.attendance;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.mgnt.sys.student.management.entity.Attendance;
import com.school.mgnt.sys.student.management.entity.Student;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

	Optional<Attendance> findByStudentAndSubjectNameAndDate(Student student, String subjectName, LocalDate date);

}

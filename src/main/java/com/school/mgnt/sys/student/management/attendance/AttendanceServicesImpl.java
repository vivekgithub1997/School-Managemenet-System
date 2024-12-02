package com.school.mgnt.sys.student.management.attendance;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.mgnt.sys.student.management.entity.Attendance;
import com.school.mgnt.sys.student.management.entity.Student;
import com.school.mgnt.sys.student.management.student.StudentRepository;
import com.school.mgnt.sys.teacher.management.teacher.Teacher;
import com.school.mgnt.sys.teacher.management.teacher.TeacherRepository;

@Service
public class AttendanceServicesImpl implements AttendanceServices {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Override
	public AttendanceResponse markAttendance(int teacherId, long registrationId, String subjectName) {
		AttendanceResponse response = new AttendanceResponse();

		try {
			// Step 1: Validate if the teacher exists
			System.out.println("Step 1: Validating teacher with ID: " + teacherId);
			Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
			if (!optionalTeacher.isPresent()) {
				System.err.println("Step 1 Failed: Teacher not found with ID: " + teacherId);
				response.setMessages("Teacher not found with ID: " + teacherId);
				return response;
			}
			Teacher teacher = optionalTeacher.get();
			System.out
					.println("Step 1 Passed: Teacher found - " + teacher.getFirstName() + " " + teacher.getLastName());

			// Step 2: Validate if the student exists with the given registration ID
			System.out.println("Step 2: Validating student with registration ID: " + registrationId);
			Optional<Student> optionalStudent = studentRepository.findById(registrationId);
			if (!optionalStudent.isPresent()) {
				System.err.println("Step 2 Failed: Student not found with registration ID: " + registrationId);
				response.setMessages("Student not found with registration ID: " + registrationId);
				return response;
			}
			Student student = optionalStudent.get();
			System.out.println("Step 2 Passed: Student found - " + student.getStudentFirstName() + " "
					+ student.getStudentLastName());

			// Step 3: Check if attendance is already marked for this student, subject, and
			// date
			System.out.println("Step 3: Checking if attendance is already marked.");
			LocalDate today = LocalDate.now();
			Optional<Attendance> existingAttendance = attendanceRepository.findByStudentAndSubjectNameAndDate(student,
					subjectName, today);
			if (existingAttendance.isPresent()) {
				System.err
						.println("Step 3 Failed: Attendance already marked for student " + student.getStudentFirstName()
								+ " " + student.getStudentLastName() + " in subject " + subjectName);
				response.setMessages("Attendance already marked for student " + student.getStudentFirstName() + " "
						+ student.getStudentLastName() + " in subject " + subjectName);
				return response;
			}
			System.out.println("Step 3 Passed: No existing attendance found. Proceeding to mark attendance.");

			// Step 4: Mark attendance
			Attendance attendance = new Attendance();
			attendance.setTeacher(teacher);
			attendance.setStudent(student);
			attendance.setStudyClass(student.getStudyClass());
			attendance.setSubjectName(subjectName);
			attendance.setTeacherName(teacher.getFirstName() + " " + teacher.getLastName());
			attendance.setDate(today);
			attendance.setAttendanceStatus("PRESENT");
			attendance.setShiftTime(student.getSchoolType() + " | " + student.getSchoolTiming());
			attendance.setRemarks("NA");

			Attendance savedAttendance = attendanceRepository.save(attendance);
			System.out.println("Step 4 Passed: Attendance marked with ID: " + savedAttendance.getAttendanceId());

			// Step 5: Populate and return response
			BeanUtils.copyProperties(savedAttendance, response, "messages", "teacher");
			response.setMessages("Attendance marked successfully.");
			// response.setTeacherName(teacher.getFirstName() + " " +
			// teacher.getLastName());
			response.setTeacherId(teacher.getTeacherId());
			response.setRegistrationId(student.getRegistrationId());
			response.setStudentName(student.getStudentFirstName() + " " + student.getStudentLastName());

			System.out.println("Step 5 Passed: All Steps Completed. Attendance marked successfully.");
		} catch (Exception e) {
			System.err.println("Error: An unexpected error occurred - " + e.getMessage());
			response.setMessages("An unexpected error occurred while marking attendance.");
			e.printStackTrace();
		}

		return response;
	}

}

package com.school.mgnt.sys.student.management.student;

import java.util.List;

public interface StudentServices {

	StudentResponse addStudent(StudentRequest studentRequest, int schoolId, String getstudyClass);

	List<StudentResponse> getAllStudentBasedOnSchoolCode(String schoolCode);

}

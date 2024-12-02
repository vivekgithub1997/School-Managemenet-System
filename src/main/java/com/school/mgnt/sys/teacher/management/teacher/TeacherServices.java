package com.school.mgnt.sys.teacher.management.teacher;

public interface TeacherServices {

	TeacherResponse addTeacher(TeacherRequest teacherRequest, int schoolId, String qualification);

}

package com.school.mgnt.sys.student.management.attendance;

public interface AttendanceServices {

	AttendanceResponse markAttendance(int teacherId, long registrationId, String subjectName);

}

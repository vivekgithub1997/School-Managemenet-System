package com.school.mgnt.sys.teacher.management.teacher;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.mgnt.sys.school.management.School;
import com.school.mgnt.sys.school.management.SchoolRepository;

@Service
public class TeacherServicesImpl implements TeacherServices {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SchoolRepository schoolRepository;

	@Override
	public TeacherResponse addTeacher(TeacherRequest teacherRequest, int schoolId,String qualification) {
		// Initialize response object
		TeacherResponse response = new TeacherResponse();

		// Check if the school exists
		School school = schoolRepository.findById(schoolId).orElse(null);
		if (school == null) {
			response.setMessage("School ID " + schoolId + " not found");
			return response;
		}

		// Map TeacherRequest to Teacher entity
		Teacher teacher = new Teacher();
		BeanUtils.copyProperties(teacherRequest, teacher);
		teacher.setSchool(school);

		teacher.setSchoolCode(school.getSchoolCode());
		teacher.setSchoolName(school.getSchoolName());
		teacher.setQualification(qualification.split(":")[0]);
		teacher.setQualificationType(qualification.split(":")[1]);
		teacher.setAvailableStatus("ONLINE");
		teacher.setCurrentStaus("OFF-CLASS");

		// setting date-of-joining , teacher-type and salary calculation

		teacher.setDateOfJoining(LocalDate.now().toString());

		teacher.setTeacherType(school.getSchoolType());

		teacher.setSalary(school.getSchoolType().equalsIgnoreCase("1-5 PRIMARY") ? 60000.0 : 70000);

		// Save the teacher entity
		Teacher savedTeacher = teacherRepository.save(teacher);

		// Map Teacher entity to TeacherResponse
		BeanUtils.copyProperties(savedTeacher, response);

		// Set additional fields in the response
		response.setSchoolId(school.getSchoolId());
		response.setMessage("Teacher added successfully");

		return response;
	}

}

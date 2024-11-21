package com.school.mgnt.sys.student.management.student;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.mgnt.sys.school.management.School;
import com.school.mgnt.sys.school.management.SchoolRepository;
import com.school.mgnt.sys.student.management.entity.Student;

@Service
public class StudentServicesImpl implements StudentServices {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private SchoolRepository schoolRepository;

	@Override
	public StudentResponse addStudent(StudentRequest studentRequest, int schoolId) {
		StudentResponse response = new StudentResponse();
		try {
			Optional<School> schoolById = schoolRepository.findById(schoolId);
			if (!schoolById.isPresent()) {

				response.setMessage("school not found this Id: " + schoolId);
				return response;
			}

			School school = schoolById.get();
			Student student = new Student();

			// using beanUtil copy source data to set destination
			BeanUtils.copyProperties(studentRequest, student);
			student.setSchool(school);

			// saved stduent

			Student saveStudent = studentRepository.save(student);

			// using beanUtil copy source data to set destination and some fields are
			// missing to set manully data
			BeanUtils.copyProperties(saveStudent, response);
			response.setMessage("One student added ✅");
			response.setGender(saveStudent.getGender().equalsIgnoreCase("Boy") ? " 👦 " : " 👧 ");
			response.setSchoolId(saveStudent.getSchool().getSchoolId());
			response.setSchoolName(saveStudent.getSchool().getSchoolName());

			return response;

		} catch (Exception e) {
			response.setMessage("Catch block : something went wrong " + e.getMessage());
		}

		return response;
	}

}

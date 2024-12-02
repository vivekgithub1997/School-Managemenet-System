package com.school.mgnt.sys.student.management.classess;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.mgnt.sys.school.management.School;
import com.school.mgnt.sys.school.management.SchoolRepository;
import com.school.mgnt.sys.student.management.entity.Classes;
import com.school.mgnt.sys.teacher.management.teacher.Teacher;
import com.school.mgnt.sys.teacher.management.teacher.TeacherRepository;

@Service
public class ClassServicesImpl implements ClassServices {

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Override
	public ClassResponse addClasses(int schoolId, String className, String resources) {
		ClassResponse response = new ClassResponse();

		try {
			// Step 1: Validate if the school exists
			System.out.println("Step 1: Validating if the school exists for ID: " + schoolId);
			Optional<School> optionalSchool = schoolRepository.findById(schoolId);
			if (!optionalSchool.isPresent()) {
				System.err.println("Step 1 Failed: School not found with ID: " + schoolId);
				response.setMessage("Step 1 Failed: School not found with ID: " + schoolId);
				return response;
			}
			School school = optionalSchool.get();
			System.out.println("Step 1 Passed: School found - " + school.getSchoolName());

			// Step 2: Validate if the className exists in the school data
			System.out.println("Step 2: Validating if the class name '" + className + "' exists in the school.");
			Classes existingClass = classRepository.findBySchoolSchoolIdAndClassName(schoolId, className);
			if (existingClass != null) {
				System.err.println("Step 2 Failed: Class with name '" + className + "' already exists in the school.");
				response.setMessage("Step 2 Failed: Class with name '" + className + "' already exists in the school.");
				return response;
			}
			System.out.println("Step 2 Passed: Class name is unique for this school.");

			// Step 3: Derive class type from school type
			System.out.println("Step 3: Validating class name against school type.");
			String schoolType = school.getSchoolType();
			int classNumber;
			try {
				classNumber = Integer.parseInt(className);
			} catch (NumberFormatException e) {
				System.err.println("Step 3 Failed: Invalid class name format. Expected numeric class name.");
				response.setMessage("Step 3 Failed: Invalid class name format. Expected numeric class name.");
				return response;
			}

			if (schoolType.equalsIgnoreCase("1-5 PRIMARY") && (classNumber < 1 || classNumber > 5)) {
				System.err.println(
						"Step 3 Failed: Class name '" + className + "' is not valid for school type '1-5 PRIMARY'.");
				response.setMessage(
						"Step 3 Failed: Class name '" + className + "' is not valid for school type '1-5 PRIMARY'.");
				return response;
			}

			if (schoolType.equalsIgnoreCase("6-9 MIDDLE") && (classNumber < 6 || classNumber > 9)) {
				System.err.println(
						"Step 3 Failed: Class name '" + className + "' is not valid for school type '6-9 MIDDLE'.");
				response.setMessage(
						"Step 3 Failed: Class name '" + className + "' is not valid for school type '6-9 MIDDLE'.");
				return response;
			}
			System.out.println("Step 3 Passed: Class name is valid for school type.");

			// Step 4: Generate classroom number (increment logic)
			System.out.println("Step 4: Generating a new classroom number.");
			int maxRoomNo = classRepository.findMaxClassRoomNoBySchoolId(schoolId).orElse(100);
			int newClassRoomNo = maxRoomNo + 1;
			System.out.println("Step 4 Passed: Generated classroom number is " + newClassRoomNo);

			// Step : Create a new Classes entity
			Classes newClass = new Classes();

			newClass.setSchool(school);
			newClass.setSchoolCode(school.getSchoolCode());
			newClass.setSchoolName(school.getSchoolName());
			newClass.setClassName(className);
			newClass.setClassType(schoolType);
			newClass.setClassRoomNo(newClassRoomNo);
			newClass.setStudentCapacity(schoolType.equalsIgnoreCase("1-5 PRIMARY") ? 25 : 30);
			newClass.setResources(resources);

			// Setting Teacher based on qualifications
			System.out.println("Step 5: Assigning a teacher to the new class.");
			List<Teacher> availableTeachers = teacherRepository.findAll().stream()
					.filter(teacher -> teacher.getClasses() == null || teacher.getClasses().isEmpty())
					.sorted((t1, t2) -> {
						int t1Rank = getQualificationRank(t1.getQualificationType());
						int t2Rank = getQualificationRank(t2.getQualificationType());
						return Integer.compare(t1Rank, t2Rank); // Ascending order of rank
					}).collect(Collectors.toList());

			if (availableTeachers.isEmpty()) {
				System.err.println("Step 5 Failed: No teacher available to assign.");
				response.setMessage("Step 5 Failed: No teacher is available to assign.");
				return response;
			}

			Teacher selectedTeacher = availableTeachers.get(0);
			newClass.setTeacher(selectedTeacher);
			System.out.println("Step 5 Passed: Assigned teacher - " + selectedTeacher.getFirstName() + " "
					+ selectedTeacher.getLastName());

			// Step : Save the new class to the database
			Classes savedClass = classRepository.save(newClass);
			System.out.println("Step 6 Passed: New class saved with ID: " + savedClass.getClassId());

			// Step 7: Map the saved class to response
			BeanUtils.copyProperties(savedClass, response);
			response.setSchoolId(schoolId);
			response.setMessage("Class added successfully");
			System.out.println("All Steps Passed: Successfully added the class.");

		} catch (Exception e) {
			System.err.println("Step Failed: An unexpected error occurred - " + e.getMessage());
			response.setMessage("Step Failed: An unexpected error occurred - " + e.getMessage());
			e.printStackTrace(); // Log the exception for debugging
		}

		return response;
	}

	// Helper method to determine rank based on qualification
	private int getQualificationRank(String qualificationType) {
		switch (qualificationType.toUpperCase()) {
		case "IITIAN":
			return 1;
		case "MTECH":
			return 2;
		case "B.ED":
			return 3;
		case "BTECH":
			return 4;
		default:
			return Integer.MAX_VALUE;
		}
	}

}

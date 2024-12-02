package com.school.mgnt.sys.school.management;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class SchoolServicesImpl implements SchoolServices {

	@Autowired
	private SchoolRepository schoolRepository;

	@Override
	public SchoolResponse addNewSchool(SchoolRequest schoolRequest, String schoolType) {
		SchoolResponse response = new SchoolResponse();
		try {
			// Check if the school with the same emailId and generated schoolCode already
			// exists
			if (schoolExists(schoolRequest)) {
				response.setMessage("A school with the same details already exists.");
				response.setSchoolId(schoolRepository.findBySchoolName(schoolRequest.getSchoolName()).getSchoolId());

				BeanUtils.copyProperties(schoolRequest, response);
				return response;
			}

			// Generate a unique school code
			String schoolCode = generateUniqueSchoolCode(schoolRequest.getSchoolName());

			// If not existing, save the new school
			School school = new School();
			BeanUtils.copyProperties(schoolRequest, school);

			// setting schoolId
			String[] split = schoolCode.split("/");
			int schoolId = Integer.parseInt(split[1]);
			school.setSchoolId(schoolId);

			// Set the generated school code
			// added current Year
			LocalDate now = LocalDate.now();
			school.setSchoolCode(schoolCode + "/" + now);

			// Set created date
			school.setCreatedDate(LocalDate.now());

			// setting school type
			school.setSchoolType(schoolType);

			// setting school timing
			school.setSchoolTiming(schoolType.equalsIgnoreCase("1-5 PRIMARY") ? "First Half :: 07:00 AM - 12:00 PM"
					: "Second Half :: 12:00 PM - 05:00 PM");

			// setting holidays
			school.setHolidays(schoolType.equalsIgnoreCase("1-5 PRIMARY") ? "50 DAYS " : "40 DAYS");

			// Save the new school
			School savedSchool = schoolRepository.save(school);

			// Copy properties of saved school to response
			BeanUtils.copyProperties(savedSchool, response);
			response.setMessage("Added ✅");
			response.setCreatedDate(savedSchool.getCreatedDate());
			response.setSchoolCode(savedSchool.getSchoolCode());

		} catch (DataAccessException e) {
			response.setMessage("Database error occurred.");
			System.out.println("Database error while saving school: " + e);
		} catch (Exception e) {
			response.setMessage("An unexpected error occurred.");
			System.out.println("Unexpected error: " + e);
		}

		return response;
	}

	private boolean schoolExists(SchoolRequest schoolRequest) {
		return schoolRepository.existsBySchoolNameAndSchoolAddressAndEmailId(schoolRequest.getSchoolName(),
				schoolRequest.getSchoolAddress(), schoolRequest.getEmailId());
	}

	private String generateUniqueSchoolCode(String schoolName) {
		// Generate the base part of the school code using the name
		String baseCode = getBest4Characters(schoolName); // Get first 3 characters of school name

		// Fetch the last School ID from the database to determine the next sequence
		Optional<School> lastSchool = schoolRepository.findTopByOrderBySchoolIdDesc();

		// Default school ID is 1 if no schools exist
		int schoolId = 1;

		if (lastSchool.isPresent()) {
			// Increment the School ID based on the last school ID
			schoolId = lastSchool.get().getSchoolId() + 1;
		}

		// Combine the base code and school ID to generate the final school code
		return baseCode + "/" + schoolId;
	}

	private String getBest4Characters(String input) {
		if (input == null || input.isEmpty()) {
			return "XXXX"; // Default if the input is null or empty
		}

		// Remove special characters and spaces, and take the first 4 valid characters
		String cleanedInput = input.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
		return cleanedInput.length() >= 4 ? cleanedInput.substring(0, 4)
				: String.format("%-4s", cleanedInput).replace(' ', 'X');
	}

	@Override
	public List<String> getAllSchoolCode() {
		return schoolRepository.findAll().stream()
				.map(school -> school.getSchoolCode() + " : " + school.getSchoolName()).collect(Collectors.toList());
	}

}

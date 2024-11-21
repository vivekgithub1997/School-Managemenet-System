package com.school.mgnt.sys.school.management;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class SchoolServicesImpl implements SchoolServices {

	@Autowired
	private SchoolRepository schoolRepository;

	@Override
	public SchoolResponse addNewSchool(SchoolRequest schoolRequest) {
		SchoolResponse response = new SchoolResponse();
		try {

			StringBuilder validationErrors = new StringBuilder();

			if (schoolRequest.getSchoolName() == null || schoolRequest.getSchoolName().equals("string")) {
				validationErrors.append("School name is required. ");
			}
			if (schoolRequest.getSchoolAddress() == null || schoolRequest.getSchoolAddress().equals("string")) {
				validationErrors.append("School address is required. ");
			}
			if (schoolRequest.getEmailId() == null || schoolRequest.getEmailId().equals("string")) {
				validationErrors.append("Email ID is required. ");
			}

			if (schoolRequest.getSchoolsHours() == null || schoolRequest.getSchoolsHours().equals("string")) {
				validationErrors.append("School hours are required. ");
			}

			if (validationErrors.length() > 0) {
				response.setMessage("Validation failed: " + validationErrors.toString().trim());
				return response;
			}

			// Check school already exists in the database

			if (schoolExists(schoolRequest)) {
				response.setMessage("A school with the same details already exists.");
				BeanUtils.copyProperties(schoolRequest, response);
				response.setSchoolId(schoolRepository.findBySchoolName(schoolRequest.getSchoolName()).getSchoolId());
				return response;
			}

			School school = new School();
			BeanUtils.copyProperties(schoolRequest, school);

			School save = schoolRepository.save(school);

			BeanUtils.copyProperties(save, response);
			response.setMessage("Added ✅");

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

}

package com.school.mgnt.sys.student.management.parent;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.mgnt.sys.student.management.entity.Parent;
import com.school.mgnt.sys.student.management.entity.Student;
import com.school.mgnt.sys.student.management.student.StudentRepository;

@Service
public class ParentServicesImpl implements ParentServices {

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Override
	public ParentResponse addParent(ParentRequest parentRequest, long registrationId, int schoolId) {
		ParentResponse response = new ParentResponse();

		try {
			// Step 1: Validate input parameters
			if (parentRequest == null || registrationId <= 0 || schoolId <= 0) {
				System.err
						.println("Step 1 Failed: Validation failed: Request, registrationId, or schoolId is invalid.");
				response.setMessage("Invalid input data: Request, registrationId, or schoolId cannot be null/zero.");
				return response;
			}
			System.out.println("Step 1 Success: Validating input parameters...");

			// Step 2: Check if student exists with the provided registrationId and schoolId
			Optional<Student> optionalStudent = studentRepository.findByRegistrationIdAndSchool_SchoolId(registrationId,
					schoolId);
			if (optionalStudent.isEmpty()) {
				System.err.println("Step 2 Failed: Mismatch: registrationId " + registrationId
						+ " does not belong to schoolId " + schoolId + ".");
				response.setMessage(
						"RegistrationId " + registrationId + " does not belong to schoolId " + schoolId + ".");
				return response;
			}

			Student student = optionalStudent.get();
			System.out.println("Step 2 Success: Validating registrationId and schoolId in the student table...");

			// Step 3: Check for duplicate email and mobile number in the Parent table
			Optional<Parent> existingParent = parentRepository.findByParentEmailIdAndMobileNumber(
					parentRequest.getParentEmailId(), parentRequest.getMobileNumber());
			if (existingParent.isPresent()) {
				if (existingParent.get().getParentEmailId().equals(parentRequest.getParentEmailId())) {
					System.err.println("Step 3 Failed: Duplicate email ID: " + parentRequest.getParentEmailId()
							+ " and Duplicate mobile number " + parentRequest.getMobileNumber());
					response.setMessage("Parent with email ID and " + parentRequest.getParentEmailId()
							+ " mobile number: " + parentRequest.getMobileNumber() + " already exists.");
					return response;
				}
				if (existingParent.get().getMobileNumber().equals(parentRequest.getMobileNumber())) {
					System.err.println("Step 3 Failed: Duplicate mobile number: " + parentRequest.getMobileNumber());
					response.setMessage(
							"Parent with mobile number " + parentRequest.getMobileNumber() + " already exists.");
					return response;
				}
			}

			// Step 4: Create a new Parent entity and copy properties from ParentRequest
			Parent parent = new Parent(); // Assuming you have a Parent entity class
			BeanUtils.copyProperties(parentRequest, parent);
			System.out.println("Step 4 Success: Creating Parent entity");

			// Step 5: Set additional properties in the Parent entity
			parent.setStudent(student);

			// Step 6: Save the Parent entity to the database using the repository
			Parent savedParent = parentRepository.save(parent);
			System.out.println("Step 6 Success: Parent entity saved successfully.");

			// Step 7: Prepare the ParentResponse object and copy properties from the saved
			// entity
			BeanUtils.copyProperties(savedParent, response);

			// Step 8: Add custom fields to the response
			response.setMessage("Parent added successfully");
			response.setRegistrationId(registrationId);
			System.out.println("Step 8 Success: ParentResponse prepared successfully with success message.");

		} catch (Exception e) {
			// Step 9: Handle generic exceptions
			System.err.println("An unexpected error occurred: " + e.getMessage());
			response.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		// Step 10: Return the response
		System.out.println("Step 10: Returning response...");
		return response;
	}

}

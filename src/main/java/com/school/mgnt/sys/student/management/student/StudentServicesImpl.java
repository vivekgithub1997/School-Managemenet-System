package com.school.mgnt.sys.student.management.student;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.school.mgnt.sys.reports.AdmissionReceiptPdfGenerator;
import com.school.mgnt.sys.school.management.School;
import com.school.mgnt.sys.school.management.SchoolRepository;
import com.school.mgnt.sys.student.management.entity.AdmissionReceipt;
import com.school.mgnt.sys.student.management.entity.Student;

@Service
public class StudentServicesImpl implements StudentServices {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private AdmissionRepository admissionRepository;

	@Override
	public StudentResponse addStudent(StudentRequest studentRequest, int schoolId, String studyClass) {
		StudentResponse response = new StudentResponse();

		try {
			// Step 1: Parse and validate study class
			int getStudyClass = parseAndValidateStudyClass(studyClass, response);
			if (getStudyClass == -1)
				return response;
			System.out.println("Step 1 completed: Study class parsed and validated.");

			// Step 2: Find and validate school
			School school = findAndValidateSchool(schoolId, getStudyClass, response);
			if (school == null)
				return response;
			System.out.println("Step 2 completed: School found and validated.");

			// Step 3: Generate a unique email ID for the student
			String studentEmail = generateStudentEmail(studentRequest.getStudentFirstName(),
					studentRequest.getStudentLastName(), school.getSchoolName());
			System.out.println("Step 3 completed: Generated unique email: " + studentEmail);

			// Step 4: Check if the child already exists for the same parent
			boolean childExists = validateChildForParentWithDOB(studentRequest, school.getSchoolId());
			if (childExists) {
				response.setMessage(
						"A child with the same name and date of birth is already registered under parent with mobile number "
								+ studentRequest.getMobileNumber() + " for this school.");
				System.err.println("Step 4 Validation failed: Duplicate student record detected.");
				return response;
			}
			System.out.println("Step 4 completed: Validation passed: No duplicate student record found.");

			// Step 5: Determine fees
			Map<String, Double> fees = calculateFees(getStudyClass);
			double admissionFee = fees.get("admissionFee");
			double monthlyFee = fees.get("monthlyFee");
			System.out.println("Step 5 completed: Fees calculated. Admission Fee: " + admissionFee + ", Monthly Fee: "
					+ monthlyFee);

			// Step 6: Generate student registration number
			Long registrationNo = generateStudentRegistrationNo();
			System.out.println("Step 6 completed: Registration number generated: " + registrationNo);

			// Step 7: Create and save student
			Student savedStudent = createAndSaveStudent(studentRequest, school, getStudyClass, registrationNo,
					studentEmail);
			if (savedStudent == null) {
				response.setMessage("Error saving student.");
				return response;
			}
			System.out.println("Step 7 completed: Student created and saved.");

			// Step 8: Generate and save receipt
			boolean receiptGenerated = generateAndSaveReceipt(savedStudent, school, admissionFee, monthlyFee,
					studyClass, response);
			if (!receiptGenerated)
				return response;
			System.out.println("Step 8 completed: Receipt generated and saved.");

			// Prepare and return response
			populateResponse(savedStudent, school, response);
			System.out.println("All steps completed successfully.");
			return response;

		} catch (Exception e) {
			System.err.println("Process failed due to: " + e.getMessage());
			e.printStackTrace();
			response.setMessage("Something went wrong: " + e.getMessage());
			return response;
		}
	}

	// Method to generate a unique email for the student
	private String generateStudentEmail(String firstName, String lastName, String schoolName) {
		// Sanitize inputs: Convert to lowercase and remove extra spaces
		String SchoolName = schoolName.toLowerCase().replaceAll("\\s+", "");
		String schoolDomain = SchoolName + ".com";

		// Create a base email using the student's full name
		String FirstName = firstName.toLowerCase().replaceAll("\\s+", "");
		String LastName = lastName.toLowerCase().replaceAll("\\s+", "");
		String baseEmail = FirstName + "." + LastName;

		// Generate the email and ensure it is unique
		String uniqueEmail = baseEmail + "@" + schoolDomain;
		int counter = 1;
		while (studentRepository.existsByStudentEmailId(uniqueEmail)) {
			uniqueEmail = baseEmail + counter + "@" + schoolDomain;
			counter++;
		}

		return uniqueEmail;
	}

	// Parse and validate the study class
	private int parseAndValidateStudyClass(String studyClass, StudentResponse response) {
		try {
			return Integer.parseInt(studyClass);
		} catch (NumberFormatException e) {
			response.setMessage("Invalid study class format: " + studyClass);
			System.err.println("Step 1 failed due to invalid study class format.");
			return -1;
		}
	}

	// Find and validate the school
	private School findAndValidateSchool(int schoolId, int studyClass, StudentResponse response) {
		Optional<School> schoolOptional = schoolRepository.findById(schoolId);
		if (!schoolOptional.isPresent()) {
			response.setMessage("School not found for ID: " + schoolId);
			System.err.println("Step 2 failed: School not found.");
			return null;
		}

		School school = schoolOptional.get();
		if ((school.getSchoolType().equalsIgnoreCase("1-5 PRIMARY") && studyClass > 5)
				|| (school.getSchoolType().equalsIgnoreCase("6-9 MIDDLE") && (studyClass < 6 || studyClass > 9))) {
			response.setMessage("Study class not supported for this school type.");
			System.err.println("Step 2 failed: Study class not supported.");
			return null;
		}
		return school;
	}

	// Check if the student already exists
	private boolean validateChildForParentWithDOB(StudentRequest studentRequest, int schoolId) {
		List<Student> existingStudents = studentRepository
				.findByStudentFirstNameAndStudentLastNameAndDateOfBirthAndMobileNumberAndSchool_SchoolId(
						studentRequest.getStudentFirstName(), studentRequest.getStudentLastName(),
						studentRequest.getDateOfBirth(), studentRequest.getMobileNumber(), schoolId);

		return !existingStudents.isEmpty();

	}

	// Calculate fees based on the study class
	private Map<String, Double> calculateFees(int studyClass) {
		Map<String, Double> fees = new HashMap<>();
		if (studyClass >= 1 && studyClass <= 5) {
			fees.put("admissionFee", 1500.0);
			fees.put("monthlyFee", 800.0);
		} else if (studyClass >= 6 && studyClass <= 9) {
			fees.put("admissionFee", 2000.0);
			fees.put("monthlyFee", 1000.0);
		} else {
			fees.put("admissionFee", 0.0);
			fees.put("monthlyFee", 0.0);
		}
		return fees;
	}

	// Generate student registration number
	public Long generateStudentRegistrationNo() {
		int currentYear = Year.now().getValue();

		Long startId = Long.parseLong(currentYear + "0000");
		Long endId = Long.parseLong(currentYear + "9999");

		Student lastStudent = studentRepository.findTopByRegistrationIdInRange(startId, endId, PageRequest.of(0, 1))
				.getContent().stream().findFirst().orElse(null);

		int newSequenceNumber = 1;

		if (lastStudent != null) {
			Long lastRegistrationId = lastStudent.getRegistrationId();
			int lastSequenceNumber = Math.toIntExact(lastRegistrationId % 10000);
			newSequenceNumber = lastSequenceNumber + 1;
		}

		String formattedSequence = String.format("%04d", newSequenceNumber);
		return Long.parseLong(currentYear + formattedSequence);
	}

	// Create and save the student
	private Student createAndSaveStudent(StudentRequest studentRequest, School school, int studyClass,
			Long registrationNo, String email) {
		try {
			Student student = new Student();
			BeanUtils.copyProperties(studentRequest, student);
			student.setRegistrationId(registrationNo);
			student.setSchool(school);
			student.setRegistrationDate(LocalDate.now());
			student.setSchoolCode(school.getSchoolCode());
			student.setAge(getAgeFromDOB(studentRequest.getDateOfBirth()));
			student.setStudyClass(String.valueOf(studyClass));
			student.setStudentEmailId(email);
			student.setStudentAddress(studentRequest.getStudentAddress());

			String[] schoolTypeAndTiming = getSchoolTypeAndTiming(studyClass).split("-");
			student.setSchoolType(schoolTypeAndTiming[0]);
			student.setSchoolTiming(schoolTypeAndTiming[1]);

			return studentRepository.save(student);
		} catch (Exception e) {
			System.err.println("Step 7 failed: Error saving student. " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private boolean generateAndSaveReceipt(Student student, School school, double admissionFee, double monthlyFee,
			String studyClass, StudentResponse response) {
		try {
			AdmissionReceipt receipt = new AdmissionReceipt();
			receipt.setReceiptId(UUID.randomUUID().toString());
			receipt.setStudentName(student.getStudentFirstName() + " " + student.getStudentLastName());
			receipt.setSchoolCode(school.getSchoolCode());
			receipt.setSchoolAddress(school.getSchoolAddress());
			receipt.setSchoolName(school.getSchoolName());
			receipt.setSchoolTiming(student.getSchoolTiming());
			receipt.setSchoolMobile(school.getMobileNumber());
			receipt.setSchoolEmail(school.getEmailId());
			receipt.setAdmissionDate(student.getRegistrationDate().toString());
			receipt.setDob(student.getDateOfBirth() + " |" + " Age " + student.getAge());
			receipt.setGender(student.getGender());
			receipt.setStudyClass(studyClass);
			receipt.setAdmissionFee(admissionFee);
			receipt.setMonthlyFee(monthlyFee);
			receipt.setStudent(student);
			receipt.setStudentEmailId(student.getStudentEmailId());

			admissionRepository.save(receipt);

			String filePath = savePdfToFileSystem(receipt);

			student.setPdfGenerated("GENERATED");
			studentRepository.save(student);
			response.setMessage("Student added successfully! Receipt saved at: " + filePath);
			return true;
		} catch (Exception e) {
			response.setMessage("Error generating or saving receipt: " + e.getMessage());
			System.err.println("Step 6 failed: Error generating receipt. " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Populate the response
	private void populateResponse(Student student, School school, StudentResponse response) {
		BeanUtils.copyProperties(student, response);
		response.setRegistrationId(student.getRegistrationId());
		response.setSchoolId(school.getSchoolId());
		response.setSchoolName(school.getSchoolName());
		response.setStudentAddress(student.getStudentAddress());
		response.setStudentEmail(student.getStudentEmailId());
	}

	private int getAgeFromDOB(String dob) {
		// input date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		// Parse the string into a LocalDate
		LocalDate dateOfBirth = LocalDate.parse(dob, formatter);

		// Get the current date
		LocalDate currentDate = LocalDate.now();

		// Calculate the age
		return Period.between(dateOfBirth, currentDate).getYears();
	}

	private String getSchoolTypeAndTiming(int studyClass) {
		String schoolType;
		String schoolTiming;

		if (studyClass >= 1 && studyClass <= 5) {
			schoolType = "PRIMARY";
			schoolTiming = "7:00 AM 12:00 PM";
		} else if (studyClass >= 6 && studyClass <= 9) {
			schoolType = "MIDDLE";
			schoolTiming = "12:00 PM 4:00 PM";
		} else {
			schoolType = "UNKNOWN";
			schoolTiming = "Not Assigned";
		}

		return schoolType + "-" + schoolTiming;
	}

	private String savePdfToFileSystem(AdmissionReceipt receipt) throws Exception {
		// Define the directory for storing receipts
		String directoryPath = "src/main/resources/receipts";
		java.nio.file.Path directory = java.nio.file.Paths.get(directoryPath);

		// Ensure the directory exists
		if (!java.nio.file.Files.exists(directory)) {
			java.nio.file.Files.createDirectories(directory);
		}

		// Define the file path for the receipt
		String fileName = receipt.getStudentName() + "_" + receipt.getStudent().getRegistrationId() + "_"
				+ receipt.getSchoolName() + ".pdf";
		java.nio.file.Path filePath = directory.resolve(fileName);

		// Generate PDF and save to the file system
		byte[] pdfData = AdmissionReceiptPdfGenerator.generatePdf(receipt);
		java.nio.file.Files.write(filePath, pdfData);

		return filePath.toAbsolutePath().toString(); // Return the absolute path to the saved file
	}

	@Override
	public List<StudentResponse> getAllStudentBasedOnSchoolCode(String schoolCode) {
		String[] split = schoolCode.split(":");
		String schoolCodeExtractSchoolName = split[0].trim();
		List<Student> bySchoolCode = studentRepository.findBySchoolCode(schoolCodeExtractSchoolName);

		int totalRecords = bySchoolCode.size(); // Total number of records

		// Use an atomic integer to keep track of the index
		AtomicInteger indexCounter = new AtomicInteger(0);

		List<StudentResponse> responses = bySchoolCode.stream().map(student -> {
			StudentResponse response = new StudentResponse();
			BeanUtils.copyProperties(student, response);
			response.setGender(student.getGender().equalsIgnoreCase("Boy") ? "👦" : "👧");
			response.setStudentEmail(student.getStudentEmailId());

			String recordIndex = (indexCounter.incrementAndGet()) + "/" + totalRecords;
			response.setMessage("Verify student ✅" + recordIndex);

			// Add school details if present
			if (student.getSchool() != null) {
				response.setSchoolId(student.getSchool().getSchoolId());
				response.setSchoolName(student.getSchool().getSchoolName());
				response.setSchoolCode(student.getSchool().getSchoolCode());
				response.setSchoolType(student.getSchool().getSchoolType());
				response.setSchoolTiming(student.getSchool().getSchoolTiming());
			}

			return response;
		}).toList();

		return responses;
	}

}

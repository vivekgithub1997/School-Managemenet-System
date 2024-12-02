package com.school.mgnt.sys.school.management;

import java.util.List;

public interface SchoolServices {

	SchoolResponse addNewSchool(SchoolRequest schoolRequest, String schoolType);

	// List<SchoolResponse> getAllSchoolsBasedOnSchoolCode(String schoolCode);

	List<String> getAllSchoolCode();

}

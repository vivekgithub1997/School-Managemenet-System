package com.school.mgnt.sys.school.management;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Integer> {

	boolean existsBySchoolNameAndSchoolAddressAndEmailId(String schoolName, String schoolAddress, String emailId);

	School findBySchoolName(String schoolName);

}

package com.school.mgnt.sys.school.management;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Integer> {

	boolean existsByEmailIdAndSchoolCode(String emailId, String schoolCode);

	boolean existsBySchoolNameAndSchoolAddressAndEmailId(String schoolNsme, String schoolAddress, String schoolEmailId);

	School findBySchoolName(String schoolName);

	boolean existsBySchoolCode(String schoolCode);

	Optional<School> findTopByOrderBySchoolIdDesc();


}

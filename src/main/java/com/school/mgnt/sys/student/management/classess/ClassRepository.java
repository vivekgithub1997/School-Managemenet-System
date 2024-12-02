package com.school.mgnt.sys.student.management.classess;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.school.mgnt.sys.student.management.entity.Classes;

public interface ClassRepository extends JpaRepository<Classes, Integer> {

	Classes findBySchoolSchoolIdAndClassName(int schoolId, String className);

	@Query("SELECT MAX(c.classRoomNo) FROM Classes c WHERE c.school.schoolId = :schoolId")
	Optional<Integer> findMaxClassRoomNoBySchoolId(@Param("schoolId") int schoolId);

}

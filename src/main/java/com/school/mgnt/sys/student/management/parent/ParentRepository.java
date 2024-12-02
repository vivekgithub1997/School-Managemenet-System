package com.school.mgnt.sys.student.management.parent;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.mgnt.sys.student.management.entity.Parent;

public interface ParentRepository extends JpaRepository<Parent, Integer> {

	Optional<Parent> findByParentEmailIdAndMobileNumber(String parentEmailId, String MobileNumber);

}

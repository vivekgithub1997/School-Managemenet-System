package com.school.mgnt.sys.student.management.parent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parent")
public class ParentController {

	@Autowired
	private ParentServices parentServices;

	@PostMapping("/add/{registrationId}/{schoolId}")
	public ResponseEntity<ParentResponse> addParent(@RequestBody ParentRequest parentRequest, long registrationId,
			int schoolId) {

		ParentResponse parent = parentServices.addParent(parentRequest, registrationId, schoolId);

		return new ResponseEntity<ParentResponse>(parent, HttpStatus.CREATED);
	}

}

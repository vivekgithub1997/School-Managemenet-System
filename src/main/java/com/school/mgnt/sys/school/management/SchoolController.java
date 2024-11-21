package com.school.mgnt.sys.school.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/school")
public class SchoolController {

	@Autowired
	private SchoolServices schoolServices;

	@GetMapping("/testing")
	public String Testing() {
		return "testing";
	}

	@PostMapping("/add/new-school")
	public ResponseEntity<SchoolResponse> RegisterNewSchool(@RequestBody SchoolRequest schoolRequest) {

		SchoolResponse newSchool = schoolServices.addNewSchool(schoolRequest);

		return new ResponseEntity<SchoolResponse>(newSchool, HttpStatus.CREATED);
	}

}

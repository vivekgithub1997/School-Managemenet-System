package com.school.mgnt.sys.student.management.classess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/classes")
public class ClassController {

	@Autowired
	private ClassServices classServices;

	@PostMapping("/add/new-classes/{schoolId}/{className}")
	public ResponseEntity<ClassResponse> addClasses(@PathVariable int schoolId,
			@Parameter(description = "classes ", required = true, schema = @Schema(allowableValues = { "1", "2", "3",
					"4", "5", "6", "7", "8", "9" })) @PathParam(value = "className") String className,
			@Parameter(description = "Resources ", required = true, schema = @Schema(allowableValues = {
					"Projector,Smart-Board,Mathematical Kits (Geometry Boxes, Models)",
					"Projector,Smart-Board,Wi-Fi Access Point", "Art Supplies (Paint, Brushes, Drawing Boards)",
					"VR (Virtual Reality) Kits",
					"Science Lab Equipment" })) @PathParam(value = "resources") String resources) {

		ClassResponse classes = classServices.addClasses(schoolId, className, resources);

		return new ResponseEntity<ClassResponse>(classes, HttpStatus.CREATED);
	}

}

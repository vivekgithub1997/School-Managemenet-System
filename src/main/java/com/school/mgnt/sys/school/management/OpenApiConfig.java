package com.school.mgnt.sys.school.management;

import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.media.StringSchema;

@Configuration
public class OpenApiConfig {

	private final SchoolServices schoolServices;

	public OpenApiConfig(SchoolServices schoolServices) {
		this.schoolServices = schoolServices;
	}

	@Bean
	public OpenApiCustomizer customOpenApi() {
		return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
			if (path.contains("/students")) {
				pathItem.readOperations().forEach(operation -> {
					operation.getParameters().stream().filter(parameter -> "schoolcode".equals(parameter.getName()))
							.forEach(parameter -> {
								// Fetch latest school codes dynamically
								List<String> schoolCodes = schoolServices.getAllSchoolCode();
								StringSchema schema = new StringSchema();
								schema.setEnum(schoolCodes);
								parameter.setSchema(schema);
							});
				});
			}
		});
	}
}

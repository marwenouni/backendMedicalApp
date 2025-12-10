package com.myapp.demo.appconfiguration.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record FormFieldDto(
	@JsonProperty("fieldName")
	String fieldName,

	@JsonProperty("label")
	String label,

	@JsonProperty("type")
	String type,

	@JsonProperty("required")
	Boolean required,

	@JsonProperty("order")
	Integer order,

	@JsonProperty("placeholder")
	String placeholder,

	@JsonProperty("options")
	List<String> options,

	@JsonProperty("minLength")
	Integer minLength,

	@JsonProperty("maxLength")
	Integer maxLength,

	@JsonProperty("min")
	Integer min,

	@JsonProperty("max")
	Integer max,

	@JsonProperty("pattern")
	String pattern,

	@JsonProperty("helpText")
	String helpText,

	@JsonProperty("visible")
	Boolean visible
) {
}

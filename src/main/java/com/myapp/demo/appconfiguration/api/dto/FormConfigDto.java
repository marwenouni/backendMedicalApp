package com.myapp.demo.appconfiguration.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

public record FormConfigDto(
	@JsonProperty("cabinetId")
	Integer cabinetId,

	@JsonProperty("formType")
	String formType,

	@JsonProperty("fields")
	List<FormFieldDto> fields,

	@JsonProperty("version")
	Integer version,

	@JsonProperty("updatedAt")
	Instant updatedAt
) {
}

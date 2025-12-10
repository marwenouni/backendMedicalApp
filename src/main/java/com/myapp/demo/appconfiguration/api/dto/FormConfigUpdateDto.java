package com.myapp.demo.appconfiguration.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record FormConfigUpdateDto(
	@JsonProperty("fields")
	List<FormFieldDto> fields
) {
}

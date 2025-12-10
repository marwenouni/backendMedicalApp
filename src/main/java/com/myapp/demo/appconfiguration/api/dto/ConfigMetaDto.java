package com.myapp.demo.appconfiguration.api.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConfigMetaDto(
	@JsonProperty("version")
	Integer version,

	@JsonProperty("lastUpdated")
	Instant lastUpdated,

	@JsonProperty("checksum")
	String checksum,

	@JsonProperty("updatedBy")
	String updatedBy
) {
}

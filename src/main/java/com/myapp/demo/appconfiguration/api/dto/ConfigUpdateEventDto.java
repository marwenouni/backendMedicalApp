package com.myapp.demo.appconfiguration.api.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConfigUpdateEventDto(
	@JsonProperty("type")
	String type,

	@JsonProperty("version")
	Integer version,

	@JsonProperty("timestamp")
	Instant timestamp,

	@JsonProperty("updatedBy")
	String updatedBy
) {
	public static ConfigUpdateEventDto update(Integer version, Instant timestamp, String updatedBy) {
		return new ConfigUpdateEventDto("update", version, timestamp, updatedBy);
	}

	public static ConfigUpdateEventDto ping(Instant timestamp) {
		return new ConfigUpdateEventDto("ping", null, timestamp, null);
	}
}

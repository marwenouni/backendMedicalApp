package com.myapp.demo.appconfiguration.api.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public record ConfigDto(
	@JsonProperty("id")
	String id,

	@JsonProperty("version")
	Integer version,

	@JsonProperty("timestamp")
	Instant timestamp,

	@JsonProperty("data")
	JsonNode data,

	@JsonProperty("meta")
	ConfigMetaDto meta
) {
}

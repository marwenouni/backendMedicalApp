package com.myapp.demo.appconfiguration.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public record ConfigDataDto(
	@JsonProperty("theme")
	ThemeDto theme,

	@JsonProperty("features")
	JsonNode features,

	@JsonProperty("localization")
	LocalizationDto localization,

	@JsonProperty("security")
	SecurityDto security,

	@JsonProperty("notifications")
	NotificationsDto notifications,

	@JsonProperty("system")
	SystemDto system
) {
}

record ThemeDto(
	@JsonProperty("primaryColor")
	String primaryColor,

	@JsonProperty("secondaryColor")
	String secondaryColor,

	@JsonProperty("mode")
	String mode,

	@JsonProperty("fontSize")
	Integer fontSize
) {
}

record LocalizationDto(
	@JsonProperty("defaultLanguage")
	String defaultLanguage,

	@JsonProperty("supportedLanguages")
	java.util.List<String> supportedLanguages,

	@JsonProperty("dateFormat")
	String dateFormat,

	@JsonProperty("timeFormat")
	String timeFormat,

	@JsonProperty("timezone")
	String timezone
) {
}

record SecurityDto(
	@JsonProperty("sessionTimeoutMinutes")
	Integer sessionTimeoutMinutes,

	@JsonProperty("maxLoginAttempts")
	Integer maxLoginAttempts,

	@JsonProperty("passwordMinLength")
	Integer passwordMinLength,

	@JsonProperty("requireMFA")
	Boolean requireMFA,

	@JsonProperty("ipWhitelist")
	String ipWhitelist
) {
}

record NotificationsDto(
	@JsonProperty("emailEnabled")
	Boolean emailEnabled,

	@JsonProperty("smsEnabled")
	Boolean smsEnabled,

	@JsonProperty("pushEnabled")
	Boolean pushEnabled,

	@JsonProperty("defaultChannels")
	java.util.List<String> defaultChannels
) {
}

record SystemDto(
	@JsonProperty("maintenanceMode")
	Boolean maintenanceMode,

	@JsonProperty("maintenanceMessage")
	String maintenanceMessage,

	@JsonProperty("apiRateLimitPerMinute")
	Integer apiRateLimitPerMinute,

	@JsonProperty("logLevel")
	String logLevel
) {
}

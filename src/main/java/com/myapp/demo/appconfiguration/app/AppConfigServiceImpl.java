package com.myapp.demo.appconfiguration.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.demo.appconfiguration.api.dto.ConfigDto;
import com.myapp.demo.appconfiguration.api.dto.ConfigMetaDto;
import com.myapp.demo.appconfiguration.api.dto.ConfigUpdateEventDto;
import com.myapp.demo.appconfiguration.api.dto.FormConfigUpdateDto;
import com.myapp.demo.appconfiguration.app.interfaces.IAppConfigService;
import com.myapp.demo.appconfiguration.domain.AppConfig;
import com.myapp.demo.appconfiguration.infra.repository.AppConfigRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class AppConfigServiceImpl implements IAppConfigService {

	private final AppConfigRepository configRepository;
	private final ObjectMapper objectMapper;

	private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public AppConfigServiceImpl(AppConfigRepository configRepository, ObjectMapper objectMapper) {
		this.configRepository = configRepository;
		this.objectMapper = objectMapper;
	}

	@PostConstruct
	public void init() {
		// Start sending ping messages every 30 seconds
		startPingService();
	}

	@PreDestroy
	public void destroy() {
		scheduler.shutdownNow();
	}

	private String getDefaultConfig() {
		return "{" +
			"\"theme\":{\"primaryColor\":\"#6366f1\",\"secondaryColor\":\"#8b5cf6\",\"mode\":\"light\",\"fontSize\":16}," +
			"\"features\":{\"darkMode\":{\"enabled\":true,\"beta\":false,\"rolloutPercentage\":100},\"chartNotes\":{\"enabled\":true,\"beta\":false},\"telemedicine\":{\"enabled\":false,\"beta\":true,\"rolloutPercentage\":30},\"advancedAnalytics\":{\"enabled\":true,\"rolloutPercentage\":100}}," +
			"\"localization\":{\"defaultLanguage\":\"en\",\"supportedLanguages\":[\"en\",\"fr\",\"es\"],\"dateFormat\":\"DD/MM/YYYY\",\"timeFormat\":\"HH:mm:ss\",\"timezone\":\"UTC\"}," +
			"\"security\":{\"sessionTimeoutMinutes\":30,\"maxLoginAttempts\":5,\"passwordMinLength\":12,\"requireMFA\":false,\"ipWhitelist\":null}," +
			"\"notifications\":{\"emailEnabled\":true,\"smsEnabled\":true,\"pushEnabled\":true,\"defaultChannels\":[\"email\",\"sms\"]}," +
			"\"system\":{\"maintenanceMode\":false,\"maintenanceMessage\":null,\"apiRateLimitPerMinute\":1000,\"logLevel\":\"info\"}," +
			"\"forms\":{" +
			"\"patient\":{\"cabinetId\":1,\"formType\":\"patient\",\"fields\":[{\"fieldName\":\"firstName\",\"label\":\"First Name\",\"type\":\"text\",\"required\":true,\"order\":1},{\"fieldName\":\"lastName\",\"label\":\"Last Name\",\"type\":\"text\",\"required\":true,\"order\":2},{\"fieldName\":\"dateOfBirth\",\"label\":\"Date of Birth\",\"type\":\"date\",\"required\":true,\"order\":3},{\"fieldName\":\"gender\",\"label\":\"Gender\",\"type\":\"select\",\"required\":true,\"order\":4,\"options\":[\"Male\",\"Female\",\"Other\"]},{\"fieldName\":\"email\",\"label\":\"Email\",\"type\":\"email\",\"required\":false,\"order\":5},{\"fieldName\":\"phone\",\"label\":\"Phone\",\"type\":\"text\",\"required\":false,\"order\":6},{\"fieldName\":\"city\",\"label\":\"City\",\"type\":\"text\",\"required\":false,\"order\":7},{\"fieldName\":\"state\",\"label\":\"State\",\"type\":\"text\",\"required\":false,\"order\":8},{\"fieldName\":\"zipCode\",\"label\":\"ZIP Code\",\"type\":\"text\",\"required\":false,\"order\":9}],\"version\":1,\"updatedAt\":\"2024-01-10T08:15:30.000Z\"}," +
			"\"encounter\":{\"cabinetId\":1,\"formType\":\"encounter\",\"fields\":[{\"fieldName\":\"chiefComplaint\",\"label\":\"Chief Complaint\",\"type\":\"textarea\",\"required\":true,\"order\":1},{\"fieldName\":\"historyOfPresentIllness\",\"label\":\"History of Present Illness\",\"type\":\"textarea\",\"required\":false,\"order\":2},{\"fieldName\":\"bpSystolic\",\"label\":\"BP Systolic\",\"type\":\"number\",\"required\":false,\"order\":3},{\"fieldName\":\"assessment\",\"label\":\"Assessment\",\"type\":\"textarea\",\"required\":true,\"order\":4},{\"fieldName\":\"plan\",\"label\":\"Plan\",\"type\":\"textarea\",\"required\":true,\"order\":5}],\"version\":1,\"updatedAt\":\"2024-01-08T14:45:20.000Z\"}," +
			"\"appointment\":{\"cabinetId\":1,\"formType\":\"appointment\",\"fields\":[{\"fieldName\":\"date\",\"label\":\"Date\",\"type\":\"date\",\"required\":true,\"order\":1},{\"fieldName\":\"time\",\"label\":\"Time\",\"type\":\"time\",\"required\":true,\"order\":2},{\"fieldName\":\"reason\",\"label\":\"Reason\",\"type\":\"textarea\",\"required\":false,\"order\":3},{\"fieldName\":\"appointmentType\",\"label\":\"Appointment Type\",\"type\":\"select\",\"required\":true,\"order\":4,\"options\":[\"Office Visit\",\"Telehealth\",\"Follow-up\"]},{\"fieldName\":\"duration\",\"label\":\"Duration (minutes)\",\"type\":\"number\",\"required\":true,\"order\":5,\"min\":15,\"max\":480}],\"version\":1,\"updatedAt\":\"2024-01-12T16:20:45.000Z\"}" +
			"}" +
		"}";
	}

	@Override
	@Transactional(readOnly = true)
	public ConfigMetaDto getConfigMeta(Long cabinetId, Long providerId) {
		return configRepository.findByCabinetIdAndProviderIdOrderByVersionDesc(cabinetId, providerId)
			.map(this::toMeta)
			.orElseThrow(() -> new RuntimeException("Configuration not found for cabinet " + cabinetId + " and provider " + providerId));
	}

	@Override
	@Transactional(readOnly = true)
	public ConfigDto getConfig(Long cabinetId, Long providerId) {
		AppConfig config = configRepository.findByCabinetIdAndProviderIdOrderByVersionDesc(cabinetId, providerId)
			.orElseGet(() -> createDefaultConfigForCabinet(cabinetId, providerId));

		try {
			JsonNode dataNode = objectMapper.readTree(config.getConfigData());
			return new ConfigDto(
				config.getConfigId(),
				config.getVersion(),
				config.getTimestamp(),
				dataNode,
				toMeta(config)
			);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse configuration", e);
		}
	}

	@Override
	@Transactional
	public void updateConfig(Long cabinetId, Long providerId, String configData, String updatedBy) {
		try {
			AppConfig currentConfig = configRepository.findByCabinetIdAndProviderIdOrderByVersionDesc(cabinetId, providerId)
				.orElseGet(() -> createDefaultConfigForCabinet(cabinetId, providerId));

			Integer newVersion = currentConfig.getVersion() + 1;
			String checksum = generateChecksum(configData);

			AppConfig newConfig = new AppConfig(
				cabinetId,
				providerId,
				"config-" + cabinetId + "-" + providerId,
				newVersion,
				configData,
				updatedBy
			);
			newConfig.setId(currentConfig.getId());
			newConfig.setChecksum(checksum);
			newConfig.setCreatedAt(currentConfig.getCreatedAt());
			AppConfig saved = configRepository.save(newConfig);

			broadcastUpdate(newVersion, saved.getLastUpdated(), updatedBy);

		} catch (Exception e) {
			throw new RuntimeException("Failed to update configuration", e);
		}
	}

	@Override
	@Transactional
	public ConfigDto updateFormConfig(Long cabinetId, Long providerId, String formType, FormConfigUpdateDto updateDto, String updatedBy) {
		try {
			AppConfig currentConfig = configRepository.findByCabinetIdAndProviderIdOrderByVersionDesc(cabinetId, providerId)
				.orElseGet(() -> createDefaultConfigForCabinet(cabinetId, providerId));

			JsonNode configDataNode = objectMapper.readTree(currentConfig.getConfigData());

			if (!configDataNode.has("forms")) {
				throw new RuntimeException("Forms configuration not found in current config");
			}

			JsonNode formsNode = configDataNode.get("forms");
			if (!formsNode.has(formType)) {
				throw new RuntimeException("Form type '" + formType + "' not found in configuration");
			}

			JsonNode formNode = formsNode.get(formType);
			com.fasterxml.jackson.databind.node.ObjectNode formObjNode = (com.fasterxml.jackson.databind.node.ObjectNode) formNode;

			Instant now = Instant.now();
			Integer currentFormVersion = formObjNode.has("version") ? formObjNode.get("version").asInt() : 1;

			formObjNode.putPOJO("fields", updateDto.fields());
			formObjNode.put("version", currentFormVersion + 1);
			formObjNode.put("updatedAt", now.toString());

			String updatedConfigData = objectMapper.writeValueAsString(configDataNode);
			Integer newVersion = currentConfig.getVersion() + 1;
			String checksum = generateChecksum(updatedConfigData);

			AppConfig newConfig = new AppConfig(
				cabinetId,
				providerId,
				"config-" + cabinetId + "-" + providerId,
				newVersion,
				updatedConfigData,
				updatedBy
			);
			newConfig.setId(currentConfig.getId());
			newConfig.setChecksum(checksum);
			newConfig.setLastUpdated(now);
			newConfig.setCreatedAt(currentConfig.getCreatedAt());
			AppConfig saved = configRepository.save(newConfig);

			broadcastUpdate(newVersion, saved.getLastUpdated(), updatedBy);

			return new ConfigDto(
				saved.getConfigId(),
				saved.getVersion(),
				saved.getTimestamp(),
				objectMapper.readTree(saved.getConfigData()),
				toMeta(saved)
			);

		} catch (com.fasterxml.jackson.core.JsonProcessingException e) {
			throw new RuntimeException("JSON processing error while updating form configuration: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("Failed to update form configuration: " + e.getMessage(), e);
		}
	}

	@Override
	public SseEmitter subscribe(Long cabinetId, Long providerId) {
		SseEmitter emitter = new SseEmitter(300000L);
		emitters.add(emitter);

		emitter.onCompletion(() -> {
			emitters.remove(emitter);
		});
		emitter.onTimeout(() -> {
			emitters.remove(emitter);
		});
		emitter.onError(throwable -> {
			emitters.remove(emitter);
		});

		return emitter;
	}

	private AppConfig createDefaultConfigForCabinet(Long cabinetId, Long providerId) {
		try {
			String configId = "config-" + cabinetId + "-" + providerId;
			String defaultConfigJson = getDefaultConfig();
			AppConfig config = new AppConfig(cabinetId, providerId, configId, 1, defaultConfigJson, "system");
			config.setChecksum(generateChecksum(defaultConfigJson));
			return configRepository.save(config);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create default config for cabinet " + cabinetId + " and provider " + providerId, e);
		}
	}

	private void startPingService() {
		scheduler.scheduleAtFixedRate(() -> {
			try {
				broadcastPing();
			} catch (Exception e) {
				// Error sending ping
			}
		}, 30, 30, TimeUnit.SECONDS);
	}

	private void broadcastUpdate(Integer version, Instant timestamp, String updatedBy) {
		ConfigUpdateEventDto event = ConfigUpdateEventDto.update(version, timestamp, updatedBy);
		broadcast(event, "update");
	}

	private void broadcastPing() {
		ConfigUpdateEventDto event = ConfigUpdateEventDto.ping(Instant.now());
		broadcast(event, "ping");
	}

	private void broadcast(ConfigUpdateEventDto event, String eventType) {
		String data;
		try {
			data = objectMapper.writeValueAsString(event);
		} catch (Exception e) {
			return;
		}

		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(SseEmitter.event()
					.id(System.currentTimeMillis() + "")
					.name(eventType)
					.data(data)
					.build());
			} catch (IOException e) {
				emitters.remove(emitter);
			}
		}
	}

	private String generateChecksum(String data) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(hash).substring(0, 32);
	}

	private ConfigMetaDto toMeta(AppConfig config) {
		return new ConfigMetaDto(
			config.getVersion(),
			config.getLastUpdated(),
			config.getChecksum(),
			config.getUpdatedBy()
		);
	}
}

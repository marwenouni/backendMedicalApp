package com.myapp.demo.appconfiguration.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.myapp.demo.appconfiguration.api.dto.ConfigDto;
import com.myapp.demo.appconfiguration.api.dto.ConfigMetaDto;
import com.myapp.demo.appconfiguration.api.dto.FormConfigUpdateDto;
import com.myapp.demo.appconfiguration.app.interfaces.IAppConfigService;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*")
public class AppConfigController {

	private final IAppConfigService appConfigService;

	public AppConfigController(IAppConfigService appConfigService) {
		this.appConfigService = appConfigService;
	}

	@GetMapping("/meta")
	public ResponseEntity<ConfigMetaDto> getConfigMeta(
			@RequestParam Long cabinetId,
			@RequestParam Long providerId) {
		try {
			ConfigMetaDto meta = appConfigService.getConfigMeta(cabinetId, providerId);
			return ResponseEntity.ok(meta);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping
	public ResponseEntity<ConfigDto> getConfig(
			@RequestParam Long cabinetId,
			@RequestParam Long providerId) {
		try {
			ConfigDto config = appConfigService.getConfig(cabinetId, providerId);
			return ResponseEntity.ok(config);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/stream")
	public SseEmitter streamConfigUpdates(
			@RequestParam Long cabinetId,
			@RequestParam Long providerId) {
		return appConfigService.subscribe(cabinetId, providerId);
	}

	@PutMapping
	public ResponseEntity<ConfigDto> updateConfig(
			@RequestParam Long cabinetId,
			@RequestParam Long providerId,
			@RequestBody String configData) {
		try {
			appConfigService.updateConfig(cabinetId, providerId, configData, "admin-user");
			ConfigDto config = appConfigService.getConfig(cabinetId, providerId);
			return ResponseEntity.ok(config);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PatchMapping("/forms/{formType}")
	public ResponseEntity<ConfigDto> updateFormConfig(
			@RequestParam Long cabinetId,
			@RequestParam Long providerId,
			@PathVariable String formType,
			@RequestBody FormConfigUpdateDto updateDto) {
		try {
			ConfigDto config = appConfigService.updateFormConfig(cabinetId, providerId, formType, updateDto, "admin-user");
			return ResponseEntity.ok(config);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}

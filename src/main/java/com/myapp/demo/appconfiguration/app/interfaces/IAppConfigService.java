package com.myapp.demo.appconfiguration.app.interfaces;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.myapp.demo.appconfiguration.api.dto.ConfigDto;
import com.myapp.demo.appconfiguration.api.dto.ConfigMetaDto;
import com.myapp.demo.appconfiguration.api.dto.FormConfigUpdateDto;

public interface IAppConfigService {

	ConfigMetaDto getConfigMeta(Long cabinetId, Long providerId);

	ConfigDto getConfig(Long cabinetId, Long providerId);

	void updateConfig(Long cabinetId, Long providerId, String configData, String updatedBy);

	ConfigDto updateFormConfig(Long cabinetId, Long providerId, String formType, FormConfigUpdateDto updateDto, String updatedBy);

	SseEmitter subscribe(Long cabinetId, Long providerId);
}

package com.myapp.demo.mapper;

import org.mapstruct.*;
import com.myapp.demo.dto.PatientDto;
import com.myapp.demo.entity.Patient;
import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface PatientMapper {

  // Entity -> DTO
  @Mappings({
      @Mapping(target = "insuranceProvider",   source = "primaryInsurance.provider"),
      @Mapping(target = "insuranceMemberId",   source = "primaryInsurance.memberId"),
      @Mapping(target = "insurancePlanName",   source = "primaryInsurance.planName"),
      @Mapping(target = "insuranceExpiration",
               source = "primaryInsurance.expirationDate",
               qualifiedByName = "localDateToString")
  })
  PatientDto toDto(Patient p);

  // DTO -> Entity
  @Mappings({
      @Mapping(target = "primaryInsurance.provider",       source = "insuranceProvider"),
      @Mapping(target = "primaryInsurance.memberId",       source = "insuranceMemberId"),
      @Mapping(target = "primaryInsurance.planName",       source = "insurancePlanName"),
      @Mapping(target = "primaryInsurance.expirationDate",
               source = "insuranceExpiration",
               qualifiedByName = "stringToLocalDate")
  })
  Patient toEntity(PatientDto dto);

  // PATCH
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(@MappingTarget Patient target, PatientDto dto);

  // ----- Converters SANS bean -----
  @Named("stringToLocalDate")
  default LocalDate stringToLocalDate(String s) {
    return (s == null || s.isBlank()) ? null : LocalDate.parse(s);
  }

  @Named("localDateToString")
  default String localDateToString(LocalDate d) {
    return d == null ? null : d.toString();
  }
}

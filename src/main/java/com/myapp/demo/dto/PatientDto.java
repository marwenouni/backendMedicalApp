package com.myapp.demo.dto;

import com.myapp.demo.entity.Patient;

public record PatientDto(
  Integer id,
  Integer idCabinet,
  String firstName,
  String lastName,
  String birthday,
  String numberPhone,
  String email,
  String city
) {
  public static PatientDto from(Patient p) {
    return new PatientDto(
      p.getId(),
      p.getIdCabinet(),
      p.getFirstName(),
      p.getLastName(),
      p.getBirthday(),
      p.getNumberPhone(),
      p.getEmail(),
      p.getCity()
    );
  }
}

package com.myapp.demo.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record PatientAggregateDto(
Long id, Long idCabinet, String clientUuid,
// identité
String firstName, String middleName, String lastName, String preferredName,
String gender, LocalDate birthday, String phoneMobile, String email,
String city, String state,
// assurance (résumé)
String insuranceProvider, String insuranceMemberId, String insurancePlanName, String insuranceExpiration,
// consentements
Boolean hipaaConsentSigned, Boolean telehealthConsentSigned, Boolean dataSharingConsent,
// clinique light
List<String> allergies, List<String> chronicConditions,
// core
Instant lastEncounterAt, Instant nextAppointmentAt
) {}

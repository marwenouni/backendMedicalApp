package com.myapp.demo.patients.api.dto;

import com.myapp.demo.entity.Patient;
import com.myapp.demo.entity.patient.PatientCore;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record PatientDto(Long id, Long idCabinet, Long idProvider,String clientUuid,

		// identity
		String firstName, String middleName, String lastName, String preferredName, String gender, LocalDate birthday,

		String photoUrl,
		// contact
		String phoneMobile, String phoneHome, String email,String addressLine1, String addressLine2 ,String city, String state,String zipCode,
        // Relation
		String maritalStatus,String emergencyContactName,String emergencyContactPhone,String emergencyContactRelation,
		//portal informations
		Boolean hasPortalAccount, String portalUsername,Instant lastPortalLogin,
		// assurance 
		String insuranceProvider, String insuranceMemberId, String insurancePlanName, LocalDate insuranceExpiration,

		// consentements
		Boolean hipaaConsentSigned, Instant hipaaConsentAt, // 🆕 date du consentement HIPAA
		Boolean telehealthConsentSigned, Boolean dataSharingConsent,Instant telehealthConsentAt,

		String bloodType,
		Double heightCm, Double weightKg, String smokingStatus, String alcoholUse,

		// SDOH
		String occupation, String employer, String housingStatus, String financialStrain, String transportationAccess,

		// allergies et conditions principales
		List<String> allergies, List<String> chronicConditions, List<String> medications,

		// derniers rendez-vous
		Instant lastEncounterAt, Instant nextAppointmentAt,Long version,Instant updatedAt) {

//	public static PatientDto from(Patient p) {
//
//		if (p == null)
//			return null;
//
//		return new PatientDto(p.getId(), p.getIdCabinet(), p.getClientUuid(), p.getFirstName(), p.getMiddleName(),
//				p.getLastName(), p.getPreferredName(), p.getGender(), p.getBirthday(), p.getPhoneMobile(),
//				p.getPhoneHome(), p.getEmail(), p.getCity(), p.getState(),
//
//				p.getPrimaryInsurance() != null ? p.getPrimaryInsurance().getProvider() : null,
//				p.getPrimaryInsurance() != null ? p.getPrimaryInsurance().getMemberId() : null,
//				p.getPrimaryInsurance() != null ? p.getPrimaryInsurance().getPlanName() : null,
//				p.getPrimaryInsurance() != null && p.getPrimaryInsurance().getExpirationDate() != null
//						? p.getPrimaryInsurance().getExpirationDate().toString()
//						: null,
//
//				p.getHipaaConsentSigned(), p.getHipaaConsentAt(), p.getTelehealthConsentSigned(),
//				p.getDataSharingConsent(), p.getHeightCm(), p.getWeightKg(), p.getSmokingStatus(), p.getAlcoholUse(),
//
//				// SDOH
//				p.getOccupation(), p.getEmployer(), p.getHousingStatus(), p.getFinancialStrain(),
//				p.getTransportationAccess(), p.getAllergies(), p.getChronicConditions(),
//
//				p.getLastEncounterAt(), p.getNextAppointmentAt());
//	}

	public Long id() {
		return id;
	}

	public Long idCabinet() {
		return idCabinet;
	}

	public Long idProvider() {
		return idProvider;
	}

	public String clientUuid() {
		return clientUuid;
	}

	public String firstName() {
		return firstName;
	}

	public String middleName() {
		return middleName;
	}

	public String lastName() {
		return lastName;
	}

	public String preferredName() {
		return preferredName;
	}

	public String gender() {
		return gender;
	}

	public LocalDate birthday() {
		return birthday;
	}

	public String phoneMobile() {
		return phoneMobile;
	}

	public String phoneHome() {
		return phoneHome;
	}

	public String email() {
		return email;
	}

	public String city() {
		return city;
	}

	public String state() {
		return state;
	}

	public String insuranceProvider() {
		return insuranceProvider;
	}

	public String insuranceMemberId() {
		return insuranceMemberId;
	}

	public String insurancePlanName() {
		return insurancePlanName;
	}

	public LocalDate insuranceExpiration() {
		return insuranceExpiration;
	}

	public Boolean hipaaConsentSigned() {
		return hipaaConsentSigned;
	}

	public Instant hipaaConsentAt() {
		return hipaaConsentAt;
	}

	public Boolean telehealthConsentSigned() {
		return telehealthConsentSigned;
	}

	public Boolean dataSharingConsent() {
		return dataSharingConsent;
	}

	public List<String> allergies() {
		return allergies;
	}

	public List<String> chronicConditions() {
		return chronicConditions;
	}

	public Instant lastEncounterAt() {
		return lastEncounterAt;
	}

	public Instant nextAppointmentAt() {
		return nextAppointmentAt;
	}

	public String photoUrl() {
		return photoUrl;
	}

	public String addressLine1() {
		return addressLine1;
	}

	public String addressLine2() {
		return addressLine2;
	}

	public String zipCode() {
		return zipCode;
	}

	public String maritalStatus() {
		return maritalStatus;
	}

	public String emergencyContactName() {
		return emergencyContactName;
	}

	public String emergencyContactPhone() {
		return emergencyContactPhone;
	}

	public String emergencyContactRelation() {
		return emergencyContactRelation;
	}

	public Boolean hasPortalAccount() {
		return hasPortalAccount;
	}

	public String portalUsername() {
		return portalUsername;
	}

	public Instant lastPortalLogin() {
		return lastPortalLogin;
	}

	public Double heightCm() {
		return heightCm;
	}

	public Double weightKg() {
		return weightKg;
	}

	public String smokingStatus() {
		return smokingStatus;
	}

	public String alcoholUse() {
		return alcoholUse;
	}

	public String occupation() {
		return occupation;
	}

	public String employer() {
		return employer;
	}

	public String housingStatus() {
		return housingStatus;
	}

	public String financialStrain() {
		return financialStrain;
	}

	public String transportationAccess() {
		return transportationAccess;
	}

	public Long version() {
		return version;
	}

	public String bloodType() {
		return bloodType;
	}

	public List<String> medications() {
		return medications;
	}

	public Instant telehealthConsentAt() {
		return telehealthConsentAt;
	}

	public Instant updatedAt() {
		return updatedAt;
	}

}

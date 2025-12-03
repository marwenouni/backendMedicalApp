package com.myapp.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(
    name = "patient",
    indexes = {
        @Index(name = "idx_patient_client_uuid", columnList = "client_uuid", unique = true),
        @Index(name = "idx_patient_last_first", columnList = "last_name, first_name"),
        @Index(name = "idx_patient_email", columnList = "email"),
        @Index(name = "idx_patient_updated_at", columnList = "updated_at")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cabinet / multi-tenancy léger */
    @Column(name = "id_cabinet")
    private Long idCabinet;

    /** UUID client (offline/PWA sync) — unique */
    @Column(name = "client_uuid", nullable = false, unique = true, length = 64)
    private String clientUuid;

    /** Identité légale / préférences d’affichage */
    @Column(name = "first_name", length = 80)
    private String firstName;

    @Column(name = "middle_name", length = 80)
    private String middleName;

    @Column(name = "last_name", length = 80)
    private String lastName;

    @Column(name = "preferred_name", length = 80)
    private String preferredName;

    /** Sexe/genre (ex: male, female, other, unknown) */
    @Column(name = "gender", length = 32)
    private String gender;

    /** Date de naissance (format médical US) */
    @Column(name = "birthday")
    private LocalDate birthday;

    /** Photo (URL signée / CDN) */
    @Column(name = "photo_url", length = 512)
    private String photoUrl;

    /** Contact — prévoir chiffrement applicatif si besoin HIPAA */
    @Column(name = "phone_mobile", length = 32)
    private String phoneMobile;

    @Column(name = "phone_home", length = 32)
    private String phoneHome;

    @Column(name = "phone_work", length = 32)
    private String phoneWork;

    @Column(name = "email", length = 160)
    private String email;

    /** Adresse postale (format US) */
    @Column(name = "address_line1", length = 160)
    private String addressLine1;

    @Column(name = "address_line2", length = 160)
    private String addressLine2;

    @Column(name = "city", length = 80)
    private String city;

    @Column(name = "state", length = 2) // ex: CA, NY, TX
    private String state;

    @Column(name = "zip_code", length = 16)
    private String zipCode;

    /** Démographie/requirements US (reporting ONC/CDC) */
    @Column(name = "marital_status", length = 32)
    private String maritalStatus; // single, married, divorced…

    @Column(name = "race", length = 64)
    private String race;          // White, Black, Asian…

    @Column(name = "ethnicity", length = 64)
    private String ethnicity;     // Hispanic / Not Hispanic

    @Column(name = "language", length = 64)
    private String language;      // preferred language

    /** Identification (SSN — ne jamais stocker complet; dernier 4 uniquement) */
    @Column(name = "ssn_last4", length = 4)
    private String ssnLast4;

    /** Contact d’urgence */
    @Column(name = "emergency_name", length = 120)
    private String emergencyContactName;

    @Column(name = "emergency_phone", length = 32)
    private String emergencyContactPhone;

    @Column(name = "emergency_relation", length = 64)
    private String emergencyContactRelation;

    /** Infos cliniques de base */
    @Column(name = "blood_type", length = 8)
    private String bloodType;

    @ElementCollection
    @CollectionTable(name = "patient_allergy", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "allergy", length = 160)
    private List<String> allergies;

    @ElementCollection
    @CollectionTable(name = "patient_medication", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "medication", length = 160)
    private List<String> medications;

    @ElementCollection
    @CollectionTable(name = "patient_chronic_condition", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "condition_name", length = 160)
    private List<String> chronicConditions;

    @Column(name = "height_cm")
    private Double heightCm;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "smoking_status", length = 32)   // never, former, current
    private String smokingStatus;

    @Column(name = "alcohol_use", length = 32)      // none, occasional, daily
    private String alcoholUse;

    /** Assurance (primaire) — embeddable proprette */
    @Embedded
    private InsuranceCoverage primaryInsurance;

    /** Pharmacie préférée (eRx) */
    @Embedded
    private PharmacyInfo preferredPharmacy;

    /** Consentements (HIPAA, téléconsultation, data sharing) */
    @Column(name = "hipaa_consent_signed")
    private Boolean hipaaConsentSigned;

    @Column(name = "hipaa_consent_at")
    private Instant hipaaConsentAt;

    @Column(name = "telehealth_consent_signed")
    private Boolean telehealthConsentSigned;

    @Column(name = "telehealth_consent_at")
    private Instant telehealthConsentAt;

    @Column(name = "data_sharing_consent")
    private Boolean dataSharingConsent;

    /** SDOH (Social Determinants of Health) */
    @Column(name = "occupation", length = 120)
    private String occupation;

    @Column(name = "employer", length = 160)
    private String employer;

    @Column(name = "housing_status", length = 64)       // stable / homeless / temporary
    private String housingStatus;

    @Column(name = "financial_strain", length = 32)     // low/medium/high
    private String financialStrain;

    @Column(name = "transport_access", length = 32)     // yes/no/limited
    private String transportationAccess;

    /** Portail patient */
    @Column(name = "has_portal_account")
    private Boolean hasPortalAccount;

    @Column(name = "portal_username", length = 120)
    private String portalUsername;

    @Column(name = "last_portal_login")
    private Instant lastPortalLogin;

    /** Audit / cycle de vie */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "last_encounter_at")
    private Instant lastEncounterAt;

    @Column(name = "next_appointment_at")
    private Instant nextAppointmentAt;

    /** Soft delete & locking optimiste */
    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Version
    @Column(name = "version")
    private Long version;

    /** Hooks JPA pour audit */
    @PrePersist
    protected void onCreate() {
        final Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.hasPortalAccount == null) this.hasPortalAccount = Boolean.FALSE;
        if (this.hipaaConsentSigned == null) this.hipaaConsentSigned = Boolean.FALSE;
        if (this.telehealthConsentSigned == null) this.telehealthConsentSigned = Boolean.FALSE;
        if (this.dataSharingConsent == null) this.dataSharingConsent = Boolean.FALSE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /** Helpers métier */
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCabinet() {
		return idCabinet;
	}

	public void setIdCabinet(Long idCabinet) {
		this.idCabinet = idCabinet;
	}

	public String getClientUuid() {
		return clientUuid;
	}

	public void setClientUuid(String clientUuid) {
		this.clientUuid = clientUuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	public String getPhoneHome() {
		return phoneHome;
	}

	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}

	public String getPhoneWork() {
		return phoneWork;
	}

	public void setPhoneWork(String phoneWork) {
		this.phoneWork = phoneWork;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSsnLast4() {
		return ssnLast4;
	}

	public void setSsnLast4(String ssnLast4) {
		this.ssnLast4 = ssnLast4;
	}

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getEmergencyContactPhone() {
		return emergencyContactPhone;
	}

	public void setEmergencyContactPhone(String emergencyContactPhone) {
		this.emergencyContactPhone = emergencyContactPhone;
	}

	public String getEmergencyContactRelation() {
		return emergencyContactRelation;
	}

	public void setEmergencyContactRelation(String emergencyContactRelation) {
		this.emergencyContactRelation = emergencyContactRelation;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public List<String> getAllergies() {
		return allergies;
	}

	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}

	public List<String> getMedications() {
		return medications;
	}

	public void setMedications(List<String> medications) {
		this.medications = medications;
	}

	public List<String> getChronicConditions() {
		return chronicConditions;
	}

	public void setChronicConditions(List<String> chronicConditions) {
		this.chronicConditions = chronicConditions;
	}

	public Double getHeightCm() {
		return heightCm;
	}

	public void setHeightCm(Double heightCm) {
		this.heightCm = heightCm;
	}

	public Double getWeightKg() {
		return weightKg;
	}

	public void setWeightKg(Double weightKg) {
		this.weightKg = weightKg;
	}

	public String getSmokingStatus() {
		return smokingStatus;
	}

	public void setSmokingStatus(String smokingStatus) {
		this.smokingStatus = smokingStatus;
	}

	public String getAlcoholUse() {
		return alcoholUse;
	}

	public void setAlcoholUse(String alcoholUse) {
		this.alcoholUse = alcoholUse;
	}

	public InsuranceCoverage getPrimaryInsurance() {
		return primaryInsurance;
	}

	public void setPrimaryInsurance(InsuranceCoverage primaryInsurance) {
		this.primaryInsurance = primaryInsurance;
	}

	public PharmacyInfo getPreferredPharmacy() {
		return preferredPharmacy;
	}

	public void setPreferredPharmacy(PharmacyInfo preferredPharmacy) {
		this.preferredPharmacy = preferredPharmacy;
	}

	public Boolean getHipaaConsentSigned() {
		return hipaaConsentSigned;
	}

	public void setHipaaConsentSigned(Boolean hipaaConsentSigned) {
		this.hipaaConsentSigned = hipaaConsentSigned;
	}

	public Instant getHipaaConsentAt() {
		return hipaaConsentAt;
	}

	public void setHipaaConsentAt(Instant hipaaConsentAt) {
		this.hipaaConsentAt = hipaaConsentAt;
	}

	public Boolean getTelehealthConsentSigned() {
		return telehealthConsentSigned;
	}

	public void setTelehealthConsentSigned(Boolean telehealthConsentSigned) {
		this.telehealthConsentSigned = telehealthConsentSigned;
	}

	public Instant getTelehealthConsentAt() {
		return telehealthConsentAt;
	}

	public void setTelehealthConsentAt(Instant telehealthConsentAt) {
		this.telehealthConsentAt = telehealthConsentAt;
	}

	public Boolean getDataSharingConsent() {
		return dataSharingConsent;
	}

	public void setDataSharingConsent(Boolean dataSharingConsent) {
		this.dataSharingConsent = dataSharingConsent;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public String getHousingStatus() {
		return housingStatus;
	}

	public void setHousingStatus(String housingStatus) {
		this.housingStatus = housingStatus;
	}

	public String getFinancialStrain() {
		return financialStrain;
	}

	public void setFinancialStrain(String financialStrain) {
		this.financialStrain = financialStrain;
	}

	public String getTransportationAccess() {
		return transportationAccess;
	}

	public void setTransportationAccess(String transportationAccess) {
		this.transportationAccess = transportationAccess;
	}

	public Boolean getHasPortalAccount() {
		return hasPortalAccount;
	}

	public void setHasPortalAccount(Boolean hasPortalAccount) {
		this.hasPortalAccount = hasPortalAccount;
	}

	public String getPortalUsername() {
		return portalUsername;
	}

	public void setPortalUsername(String portalUsername) {
		this.portalUsername = portalUsername;
	}

	public Instant getLastPortalLogin() {
		return lastPortalLogin;
	}

	public void setLastPortalLogin(Instant lastPortalLogin) {
		this.lastPortalLogin = lastPortalLogin;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Instant getLastEncounterAt() {
		return lastEncounterAt;
	}

	public void setLastEncounterAt(Instant lastEncounterAt) {
		this.lastEncounterAt = lastEncounterAt;
	}

	public Instant getNextAppointmentAt() {
		return nextAppointmentAt;
	}

	public void setNextAppointmentAt(Instant nextAppointmentAt) {
		this.nextAppointmentAt = nextAppointmentAt;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Instant deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
    
    
}

package com.myapp.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(
    name = "provider",
    indexes = {
        @Index(name = "idx_provider_client_uuid", columnList = "client_uuid", unique = true),
        @Index(name = "idx_provider_npi", columnList = "npi", unique = true),
        @Index(name = "idx_provider_last_first", columnList = "last_name, first_name"),
        @Index(name = "idx_provider_updated_at", columnList = "updated_at"),
        @Index(name = "idx_provider_dea", columnList = "dea_number")
    }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Rattachement cabinet / multi-tenant léger (optionnel) */
    @Column(name = "id_cabinet")
    private Long idCabinet;

    /** UUID client pour sync offline (unique app-side) */
    @Column(name = "client_uuid", nullable = false, unique = true, length = 64)
    private String clientUuid;

    /** Identité légale */
    @Column(name = "first_name", length = 80)  private String firstName;
    @Column(name = "middle_name", length = 80) private String middleName;
    @Column(name = "last_name", length = 80)   private String lastName;
    @Column(name = "suffix", length = 20)      private String suffix;     // Jr, Sr, III…
    @Column(name = "credentials", length = 40) private String credentials; // MD, DO, NP, PA, DDS…
    @Column(name = "gender", length = 32)      private String gender;      // male/female/other/unknown
    @Column(name = "dob")                      private LocalDate dob;

    /** Contact pro */
    @Column(name = "email", length = 160)      private String email;
    @Column(name = "phone_work", length = 32)  private String phoneWork;
    @Column(name = "phone_mobile", length = 32)private String phoneMobile;
    @Column(name = "fax", length = 32)         private String fax;
    @Column(name = "website", length = 160)    private String website;

    /** Adresse professionnelle */
    @Embedded
    private Address address;

    /** Profil clinique / facturation US */
    @Column(name = "npi", length = 10, unique = true)  private String npi;          // National Provider Identifier
    @Column(name = "taxonomy_code", length = 20)       private String taxonomyCode; // ex: 207Q00000X (Family Medicine)
    @Column(name = "specialty_primary", length = 120)  private String specialtyPrimary;

    @ElementCollection
    @CollectionTable(name = "provider_specialty", joinColumns = @JoinColumn(name = "provider_id"))
    @Column(name = "specialty", length = 120)
    private List<String> specialties; // secondaires

    /** Licences d’État (obligatoire US) */
    @ElementCollection
    @CollectionTable(name = "provider_license", joinColumns = @JoinColumn(name = "provider_id"))
    private List<ProviderLicense> stateLicenses;

    /** DEA (prescription substances contrôlées) */
    @Column(name = "dea_number", length = 32)
    private String deaNumber;

    @Column(name = "dea_expires_on")
    private LocalDate deaExpiresOn;

    /** EPCS (e-prescribing controlled substances) */
    @Column(name = "epcs_enabled")
    private Boolean epcsEnabled;

    @Column(name = "epcs_vendor", length = 120)
    private String epcsVendor;

    @Column(name = "epcs_registered_at")
    private Instant epcsRegisteredAt;

    /** eRx (Surescripts) */
    @Column(name = "erx_enabled")
    private Boolean erxEnabled;

    @Column(name = "erx_spi_id", length = 32)
    private String erxSpiId; // Surescripts SPI

    @Column(name = "erx_vendor", length = 120)
    private String erxVendor;

    /** Identifiants clearinghouse / billing group */
    @Column(name = "clearinghouse_submitter_id", length = 64)
    private String clearinghouseSubmitterId;

    @Column(name = "payer_id_default", length = 16)
    private String payerIdDefault;

    /** Group / Organization NPI (si facturation sous entité) */
    @Column(name = "group_npi", length = 10)
    private String groupNpi;

    /** EIN (Tax ID) — stocker partiel si possible */
    @Column(name = "tax_id_last4", length = 4)
    private String taxIdLast4;

    /** CLIA (si lab in-office) */
    @Column(name = "clia_number", length = 20)
    private String cliaNumber;

    /** Téléconsultation */
    @Column(name = "telehealth_enabled")
    private Boolean telehealthEnabled;

    /** Signature (image, pour ordonnances) */
    @Column(name = "signature_image_url", length = 512)
    private String signatureImageUrl;

    /** Audit / cycle de vie */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Version
    @Column(name = "version")
    private Long version;

    /** Hooks JPA */
    @PrePersist
    protected void onCreate() {
        final Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.erxEnabled == null) this.erxEnabled = Boolean.FALSE;
        if (this.epcsEnabled == null) this.epcsEnabled = Boolean.FALSE;
        if (this.telehealthEnabled == null) this.telehealthEnabled = Boolean.FALSE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /** Helpers */
    @Transient
    public boolean isDeleted() { return deletedAt != null; }

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

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneWork() {
		return phoneWork;
	}

	public void setPhoneWork(String phoneWork) {
		this.phoneWork = phoneWork;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getNpi() {
		return npi;
	}

	public void setNpi(String npi) {
		this.npi = npi;
	}

	public String getTaxonomyCode() {
		return taxonomyCode;
	}

	public void setTaxonomyCode(String taxonomyCode) {
		this.taxonomyCode = taxonomyCode;
	}

	public String getSpecialtyPrimary() {
		return specialtyPrimary;
	}

	public void setSpecialtyPrimary(String specialtyPrimary) {
		this.specialtyPrimary = specialtyPrimary;
	}

	public List<String> getSpecialties() {
		return specialties;
	}

	public void setSpecialties(List<String> specialties) {
		this.specialties = specialties;
	}

	public List<ProviderLicense> getStateLicenses() {
		return stateLicenses;
	}

	public void setStateLicenses(List<ProviderLicense> stateLicenses) {
		this.stateLicenses = stateLicenses;
	}

	public String getDeaNumber() {
		return deaNumber;
	}

	public void setDeaNumber(String deaNumber) {
		this.deaNumber = deaNumber;
	}

	public LocalDate getDeaExpiresOn() {
		return deaExpiresOn;
	}

	public void setDeaExpiresOn(LocalDate deaExpiresOn) {
		this.deaExpiresOn = deaExpiresOn;
	}

	public Boolean getEpcsEnabled() {
		return epcsEnabled;
	}

	public void setEpcsEnabled(Boolean epcsEnabled) {
		this.epcsEnabled = epcsEnabled;
	}

	public String getEpcsVendor() {
		return epcsVendor;
	}

	public void setEpcsVendor(String epcsVendor) {
		this.epcsVendor = epcsVendor;
	}

	public Instant getEpcsRegisteredAt() {
		return epcsRegisteredAt;
	}

	public void setEpcsRegisteredAt(Instant epcsRegisteredAt) {
		this.epcsRegisteredAt = epcsRegisteredAt;
	}

	public Boolean getErxEnabled() {
		return erxEnabled;
	}

	public void setErxEnabled(Boolean erxEnabled) {
		this.erxEnabled = erxEnabled;
	}

	public String getErxSpiId() {
		return erxSpiId;
	}

	public void setErxSpiId(String erxSpiId) {
		this.erxSpiId = erxSpiId;
	}

	public String getErxVendor() {
		return erxVendor;
	}

	public void setErxVendor(String erxVendor) {
		this.erxVendor = erxVendor;
	}

	public String getClearinghouseSubmitterId() {
		return clearinghouseSubmitterId;
	}

	public void setClearinghouseSubmitterId(String clearinghouseSubmitterId) {
		this.clearinghouseSubmitterId = clearinghouseSubmitterId;
	}

	public String getPayerIdDefault() {
		return payerIdDefault;
	}

	public void setPayerIdDefault(String payerIdDefault) {
		this.payerIdDefault = payerIdDefault;
	}

	public String getGroupNpi() {
		return groupNpi;
	}

	public void setGroupNpi(String groupNpi) {
		this.groupNpi = groupNpi;
	}

	public String getTaxIdLast4() {
		return taxIdLast4;
	}

	public void setTaxIdLast4(String taxIdLast4) {
		this.taxIdLast4 = taxIdLast4;
	}

	public String getCliaNumber() {
		return cliaNumber;
	}

	public void setCliaNumber(String cliaNumber) {
		this.cliaNumber = cliaNumber;
	}

	public Boolean getTelehealthEnabled() {
		return telehealthEnabled;
	}

	public void setTelehealthEnabled(Boolean telehealthEnabled) {
		this.telehealthEnabled = telehealthEnabled;
	}

	public String getSignatureImageUrl() {
		return signatureImageUrl;
	}

	public void setSignatureImageUrl(String signatureImageUrl) {
		this.signatureImageUrl = signatureImageUrl;
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

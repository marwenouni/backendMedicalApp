package com.myapp.demo.charts.domain;


import java.time.Instant;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Index;


@Entity @Table(name="chart_identity",
indexes = {
 @Index(name="idx_ident_last_first", columnList="last_name, first_name"),
 @Index(name="idx_ident_email", columnList="email")
})
@Getter @Setter
public class ChartIdentity {
@Id private Long id;

@MapsId @OneToOne @JoinColumn(name="core_id") private ChartCore core;

@Column(name="first_name", length=80)   private String firstName;
@Column(name="middle_name", length=80)  private String middleName;
@Column(name="last_name", length=80)    private String lastName;
@Column(name="preferred_name", length=80) private String preferredName;
@Column(name="gender", length=32)       private String gender;
@Column(name="birthday")                private LocalDate birthday;
@Column(name="photo_url", length=512)   private String photoUrl;

@Column(name="phone_mobile", length=32) private String phoneMobile;
@Column(name="phone_home", length=32)   private String phoneHome;
@Column(name="email", length=160)       private String email;

@Column(name="address_line1", length=160) private String addressLine1;
@Column(name="address_line2", length=160) private String addressLine2;
@Column(name="city", length=80)           private String city;
@Column(name="state", length=2)           private String state;
@Column(name="zip_code", length=16)       private String zipCode;

@Column(name="marital_status", length=32) private String maritalStatus;
@Column(name="race", length=64)           private String race;
@Column(name="language", length=64)       private String language;

@Column(name="ssn_last4", length=4)       private String ssnLast4;

@Column(name="emergency_name", length=120)    private String emergencyContactName;
@Column(name="emergency_phone", length=32)    private String emergencyContactPhone;
@Column(name="emergency_relation", length=64) private String emergencyContactRelation;

@Column(name="has_portal_account") private Boolean hasPortalAccount = Boolean.FALSE;
@Column(name="portal_username", length=120) private String portalUsername;
@Column(name="last_portal_login") private Instant lastPortalLogin;
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public ChartCore getCore() {
	return core;
}
public void setCore(ChartCore core) {
	this.core = core;
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


}

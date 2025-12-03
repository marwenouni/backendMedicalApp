package com.myapp.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class InsuranceCoverage {

    @Column(name = "ins_provider", length = 120)
    private String provider;              // Aetna, BCBS, Medicare…

    @Column(name = "ins_member_id", length = 64)
    private String memberId;

    @Column(name = "ins_group_number", length = 64)
    private String groupNumber;

    @Column(name = "ins_plan_name", length = 120)
    private String planName;

    @Column(name = "ins_expiration")
    private LocalDate expirationDate;

    @Column(name = "ins_copay", precision = 10, scale = 2)
    private BigDecimal copayAmount;

    @Column(name = "ins_coinsurance", length = 16)
    private String coinsurance;           // ex: "20%"

    @Column(name = "ins_deductible", precision = 12, scale = 2)
    private BigDecimal deductible;        // franchise restant

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BigDecimal getCopayAmount() {
		return copayAmount;
	}

	public void setCopayAmount(BigDecimal copayAmount) {
		this.copayAmount = copayAmount;
	}

	public String getCoinsurance() {
		return coinsurance;
	}

	public void setCoinsurance(String coinsurance) {
		this.coinsurance = coinsurance;
	}

	public BigDecimal getDeductible() {
		return deductible;
	}

	public void setDeductible(BigDecimal deductible) {
		this.deductible = deductible;
	}
    
    
}

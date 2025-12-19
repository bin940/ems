package com.datz.ems.energy.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ess_status")
public class EssStatus {
  @Id
  @UuidGenerator
  private UUID id;

  @Column(name = "company_id", nullable = false)
  private UUID companyId;

  @Column(name = "facility_id", nullable = false)
  private UUID facilityId;

  @Column(name = "recorded_at", nullable = false)
  private Instant recordedAt;

  @Column(name = "soc", nullable = false)
  private double soc;

  @Column(name = "power_kw", nullable = false)
  private double powerKw;

  public UUID getId() {
    return id;
  }

  public UUID getCompanyId() {
    return companyId;
  }

  public void setCompanyId(UUID companyId) {
    this.companyId = companyId;
  }

  public UUID getFacilityId() {
    return facilityId;
  }

  public void setFacilityId(UUID facilityId) {
    this.facilityId = facilityId;
  }

  public Instant getRecordedAt() {
    return recordedAt;
  }

  public void setRecordedAt(Instant recordedAt) {
    this.recordedAt = recordedAt;
  }

  public double getSoc() {
    return soc;
  }

  public void setSoc(double soc) {
    this.soc = soc;
  }

  public double getPowerKw() {
    return powerKw;
  }

  public void setPowerKw(double powerKw) {
    this.powerKw = powerKw;
  }
}

package com.datz.ems.energy.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "energy_usage")
public class EnergyUsage {
  @Id
  @UuidGenerator
  private UUID id;

  @Column(name = "company_id", nullable = false)
  private UUID companyId;

  @Column(name = "facility_id", nullable = false)
  private UUID facilityId;

  @Column(name = "measured_at", nullable = false)
  private Instant measuredAt;

  @Column(name = "usage_kwh", nullable = false)
  private double usageKwh;

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

  public Instant getMeasuredAt() {
    return measuredAt;
  }

  public void setMeasuredAt(Instant measuredAt) {
    this.measuredAt = measuredAt;
  }

  public double getUsageKwh() {
    return usageKwh;
  }

  public void setUsageKwh(double usageKwh) {
    this.usageKwh = usageKwh;
  }
}

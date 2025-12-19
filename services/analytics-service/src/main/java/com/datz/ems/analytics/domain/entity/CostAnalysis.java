package com.datz.ems.analytics.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cost_analysis")
public class CostAnalysis {
  @Id
  @UuidGenerator
  private UUID id;

  @Column(name = "company_id", nullable = false)
  private UUID companyId;

  @Column(name = "facility_id", nullable = false)
  private UUID facilityId;

  @Column(name = "period_start", nullable = false)
  private LocalDate periodStart;

  @Column(name = "period_end", nullable = false)
  private LocalDate periodEnd;

  @Column(name = "before_cost", nullable = false)
  private double beforeCost;

  @Column(name = "after_cost", nullable = false)
  private double afterCost;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

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

  public LocalDate getPeriodStart() {
    return periodStart;
  }

  public void setPeriodStart(LocalDate periodStart) {
    this.periodStart = periodStart;
  }

  public LocalDate getPeriodEnd() {
    return periodEnd;
  }

  public void setPeriodEnd(LocalDate periodEnd) {
    this.periodEnd = periodEnd;
  }

  public double getBeforeCost() {
    return beforeCost;
  }

  public void setBeforeCost(double beforeCost) {
    this.beforeCost = beforeCost;
  }

  public double getAfterCost() {
    return afterCost;
  }

  public void setAfterCost(double afterCost) {
    this.afterCost = afterCost;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}

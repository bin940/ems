package com.datz.ems.energy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "charge_rule")
public class ChargeRule {
  @Id
  @UuidGenerator
  private UUID id;

  @Column(name = "policy_id", nullable = false)
  private UUID policyId;

  @Column(name = "start_hour", nullable = false)
  private int startHour;

  @Column(name = "end_hour", nullable = false)
  private int endHour;

  @Column(name = "max_kw", nullable = false)
  private double maxKw;

  public UUID getId() {
    return id;
  }

  public UUID getPolicyId() {
    return policyId;
  }

  public void setPolicyId(UUID policyId) {
    this.policyId = policyId;
  }

  public int getStartHour() {
    return startHour;
  }

  public void setStartHour(int startHour) {
    this.startHour = startHour;
  }

  public int getEndHour() {
    return endHour;
  }

  public void setEndHour(int endHour) {
    this.endHour = endHour;
  }

  public double getMaxKw() {
    return maxKw;
  }

  public void setMaxKw(double maxKw) {
    this.maxKw = maxKw;
  }
}

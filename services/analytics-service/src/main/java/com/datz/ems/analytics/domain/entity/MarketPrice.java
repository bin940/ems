package com.datz.ems.analytics.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "market_price")
public class MarketPrice {
  @Id
  @UuidGenerator
  private UUID id;

  @Column(name = "market_date", nullable = false)
  private LocalDate marketDate;

  @Column(name = "hour", nullable = false)
  private int hour;

  @Column(name = "smp", nullable = false)
  private double smp;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  public UUID getId() {
    return id;
  }

  public LocalDate getMarketDate() {
    return marketDate;
  }

  public void setMarketDate(LocalDate marketDate) {
    this.marketDate = marketDate;
  }

  public int getHour() {
    return hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public double getSmp() {
    return smp;
  }

  public void setSmp(double smp) {
    this.smp = smp;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}

package com.datz.ems.energy.api.dto;

import java.util.UUID;

public class FacilityResponse {
  private UUID id;
  private String name;
  private String location;

  public FacilityResponse(UUID id, String name, String location) {
    this.id = id;
    this.name = name;
    this.location = location;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getLocation() {
    return location;
  }
}

package com.datz.ems.energy.api;

import com.datz.ems.energy.config.AuthContext;
import com.datz.ems.energy.domain.EssStatus;
import com.datz.ems.energy.domain.EnergyUsage;
import com.datz.ems.energy.domain.Facility;
import com.datz.ems.energy.infra.EssStatusRepository;
import com.datz.ems.energy.infra.EnergyUsageRepository;
import com.datz.ems.energy.infra.FacilityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/facilities")
public class FacilityController {
  private final FacilityRepository facilityRepository;
  private final EnergyUsageRepository energyUsageRepository;
  private final EssStatusRepository essStatusRepository;

  public FacilityController(
      FacilityRepository facilityRepository,
      EnergyUsageRepository energyUsageRepository,
      EssStatusRepository essStatusRepository) {
    this.facilityRepository = facilityRepository;
    this.energyUsageRepository = energyUsageRepository;
    this.essStatusRepository = essStatusRepository;
  }

  @GetMapping
  public List<FacilityResponse> listFacilities() {
    UUID companyId = AuthContext.companyId();
    return facilityRepository.findByCompanyId(companyId).stream()
        .map(f -> new FacilityResponse(f.getId(), f.getName(), f.getLocation()))
        .toList();
  }

  @PostMapping
  public ResponseEntity<FacilityResponse> createFacility(@RequestBody FacilityCreateRequest request) {
    UUID companyId = AuthContext.companyId();
    Facility facility = new Facility();
    facility.setCompanyId(companyId);
    facility.setName(request.getName());
    facility.setLocation(request.getLocation());
    Facility saved = facilityRepository.save(facility);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new FacilityResponse(saved.getId(), saved.getName(), saved.getLocation()));
  }

  @GetMapping("/{facilityId}/usage")
  public List<EnergyUsage> usage(@PathVariable UUID facilityId) {
    UUID companyId = AuthContext.companyId();
    return energyUsageRepository.findByCompanyIdAndFacilityId(companyId, facilityId);
  }

  @GetMapping("/{facilityId}/ess-status")
  public List<EssStatus> essStatus(@PathVariable UUID facilityId) {
    UUID companyId = AuthContext.companyId();
    return essStatusRepository.findByCompanyIdAndFacilityId(companyId, facilityId);
  }
}

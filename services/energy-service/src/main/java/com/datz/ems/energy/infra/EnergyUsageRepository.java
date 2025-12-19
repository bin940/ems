package com.datz.ems.energy.infra;

import com.datz.ems.energy.domain.EnergyUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EnergyUsageRepository extends JpaRepository<EnergyUsage, UUID> {
  List<EnergyUsage> findByCompanyIdAndFacilityId(UUID companyId, UUID facilityId);
}

package com.datz.ems.energy.infra;

import com.datz.ems.energy.domain.EssStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EssStatusRepository extends JpaRepository<EssStatus, UUID> {
  List<EssStatus> findByCompanyIdAndFacilityId(UUID companyId, UUID facilityId);
}

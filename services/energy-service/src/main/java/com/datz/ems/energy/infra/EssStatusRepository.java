package com.datz.ems.energy.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datz.ems.energy.domain.entity.EssStatus;

import java.util.List;
import java.util.UUID;

public interface EssStatusRepository extends JpaRepository<EssStatus, UUID> {
  List<EssStatus> findByCompanyIdAndFacilityId(UUID companyId, UUID facilityId);
}

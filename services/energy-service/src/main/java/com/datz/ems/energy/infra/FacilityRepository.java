package com.datz.ems.energy.infra;

import com.datz.ems.energy.domain.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FacilityRepository extends JpaRepository<Facility, UUID> {
  List<Facility> findByCompanyId(UUID companyId);
}

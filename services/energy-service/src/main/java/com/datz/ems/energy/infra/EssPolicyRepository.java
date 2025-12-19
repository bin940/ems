package com.datz.ems.energy.infra;

import com.datz.ems.energy.domain.EssPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EssPolicyRepository extends JpaRepository<EssPolicy, UUID> {
  List<EssPolicy> findByCompanyId(UUID companyId);
}

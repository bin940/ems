CREATE TABLE facility (
  id UUID PRIMARY KEY,
  company_id UUID NOT NULL,
  name VARCHAR(200) NOT NULL,
  location VARCHAR(200),
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE energy_usage (
  id UUID PRIMARY KEY,
  company_id UUID NOT NULL,
  facility_id UUID NOT NULL,
  measured_at TIMESTAMP NOT NULL,
  usage_kwh DOUBLE PRECISION NOT NULL
);

CREATE TABLE ess_status (
  id UUID PRIMARY KEY,
  company_id UUID NOT NULL,
  facility_id UUID NOT NULL,
  recorded_at TIMESTAMP NOT NULL,
  soc DOUBLE PRECISION NOT NULL,
  power_kw DOUBLE PRECISION NOT NULL
);

CREATE TABLE ess_policy (
  id UUID PRIMARY KEY,
  company_id UUID NOT NULL,
  name VARCHAR(200) NOT NULL,
  enabled BOOLEAN NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE charge_rule (
  id UUID PRIMARY KEY,
  policy_id UUID NOT NULL,
  start_hour INT NOT NULL,
  end_hour INT NOT NULL,
  max_kw DOUBLE PRECISION NOT NULL
);

CREATE TABLE discharge_rule (
  id UUID PRIMARY KEY,
  policy_id UUID NOT NULL,
  start_hour INT NOT NULL,
  end_hour INT NOT NULL,
  min_soc DOUBLE PRECISION NOT NULL
);

CREATE INDEX idx_facility_company_id ON facility(company_id);
CREATE INDEX idx_energy_usage_company_facility ON energy_usage(company_id, facility_id);
CREATE INDEX idx_ess_status_company_facility ON ess_status(company_id, facility_id);
CREATE INDEX idx_ess_policy_company_id ON ess_policy(company_id);

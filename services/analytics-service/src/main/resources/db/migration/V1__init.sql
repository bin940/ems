CREATE TABLE market_price (
  id UUID PRIMARY KEY,
  market_date DATE NOT NULL,
  hour INT NOT NULL,
  smp DOUBLE PRECISION NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE cost_analysis (
  id UUID PRIMARY KEY,
  company_id UUID NOT NULL,
  facility_id UUID NOT NULL,
  period_start DATE NOT NULL,
  period_end DATE NOT NULL,
  before_cost DOUBLE PRECISION NOT NULL,
  after_cost DOUBLE PRECISION NOT NULL,
  created_at TIMESTAMP NOT NULL
);

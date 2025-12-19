CREATE TABLE company (
  id UUID PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE TABLE users (
  id UUID PRIMARY KEY,
  email VARCHAR(200) NOT NULL UNIQUE,
  password_hash VARCHAR(200) NOT NULL,
  role VARCHAR(50) NOT NULL,
  company_id UUID NOT NULL,
  created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_users_company_id ON users(company_id);

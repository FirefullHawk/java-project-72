DROP TABLE IF EXISTS url_checks;
DROP TABLE IF EXISTS urls;

create table urls (
  id                            SERIAL PRIMARY KEY,
  name                          VARCHAR(255) not null,
  created_at                    timestamp not null
  );

create table url_checks (
  id                            SERIAL PRIMARY KEY,
  status_code                   INTEGER not null,
  title                         VARCHAR(255),
  h1                            VARCHAR(255),
  description                   TEXT,
  url_id                        BIGINT REFERENCES urls (id),
  created_at                    TIMESTAMP not null
);
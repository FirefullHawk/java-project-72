DROP TABLE IF EXISTS urls;

create table urls (
  id                            SERIAL PRIMARY KEY,
  name                          varchar(255) not null,
  created_at                    timestamp not null
);

DROP TABLE IF EXISTS url_checks;

create table url_checks (
  id                            SERIAL PRIMARY KEY,
  status_code                   int not null,
  title                         varchar(255),
  h1                            varchar(255),
  description                   text,
  url_id                        int,
  created_at                    timestamp not null
);

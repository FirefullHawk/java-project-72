DROP TABLE IF EXISTS urls;

create table urls (
  id                            SERIAL,
  name                          varchar(255) not null,
  created_at                    timestamp not null,
  primary key (id)
);

DROP TABLE IF EXISTS url_checks;

create table url_checks (
  id                            SERIAL,
  status_code                   int not null,
  title                         varchar(255),
  h1                            varchar(255),
  description                   text,
  url_id                        bigint,
  created_at                    timestamp not null,
  primary key (id)
);
create table tippspiel_user (
  user_id   int         not null,
  user_name varchar(50) NOT NULL,
  password  varchar(500) NOT NULL,
  enabled   boolean,
  primary key (user_id)
);

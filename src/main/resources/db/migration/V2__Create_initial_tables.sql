CREATE TABLE team (
  id int NOT NULL,
  name varchar(200) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE tournament (
  id varchar(50) NOT NULL,
  name varchar(100) NOT NULL,
  score_range varchar(50),
  fk_champion_id int references team(id),
  PRIMARY KEY (id)
);

CREATE TABLE game (
  id int not null,
  order_pos int not null,
  fk_team1_id int not null references team(id),
  fk_team2_id int not null references team(id),
  score1 int not null default -1,
  score2 int not null default -1,
  fk_tournament_id varchar(50) not null references tournament(id),
  PRIMARY KEY (id)
);

CREATE TABLE player (
  id int not null,
  firstname varchar(200) not null,
  lastname varchar(200) not null,
  street varchar(200) not null,
  zip varchar(10) not null,
  city varchar(200) not null,
  score_range varchar(50) not null,
  guessed_top_scorer varchar(200) not null,
  fk_tournament_id varchar(50) not null references tournament(id),
  fk_champion_id int not null references team(id),
  fk_user_id int not null references tippspiel_user(user_id),
  PRIMARY KEY (id)
);


CREATE TABLE bet (
  id int not null,
  fk_game_id int not null references game(id),
  fk_player_id int not null references player(id),
  score1 int not null,
  score2 int not null,
  PRIMARY KEY (id)
);

CREATE TABLE scorer (
  id int not null,
  name varchar(200) not null,
  fk_tournament_id varchar(50) not null references tournament(id),
  PRIMARY KEY (id)
);

CREATE SEQUENCE bet_id_seq;
CREATE SEQUENCE game_id_seq;
CREATE SEQUENCE player_id_seq;
CREATE SEQUENCE scorer_id_seq;




drop table if exists play_player cascade;
drop table if exists play cascade;
drop table if exists game cascade;
drop table if exists player cascade;

create table player(
	id serial not null primary key, 
	external_id int not null unique,
	first_name varchar(20) not null,
	last_name varchar(20) not null,
	short_name varchar(40) not null unique);


create table game(
	id serial not null primary key, 
	code varchar(20) not null unique,
	home_team varchar(20) not null,
	visitor_team varchar(20) not null,

	start_time time,
	end_time time,
	finished boolean
);

create table play(
	id serial not null primary key, 
	external_id int not null,

	game_id int not null references game,

	quarter int not null,
	quarter_time varchar(5) not null,
	game_time varchar(5) not null,
	description varchar(200) not null,

	home_score int not null,
	visitor_score int not null,

	unique(game_id, external_id)
);

create table play_player(
	player_id int not null references player,
	play_id int not null references play,
	position int not null,

	primary key (player_id, play_id),
	unique (player_id, play_id, position)
);

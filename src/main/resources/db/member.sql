create table member (
	id serial primary key,
	user_name varchar,
	password varchar,
	name varchar,
	authority varchar[]
)
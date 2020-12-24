create table post (
	id serial primary key,
	title varchar,
	content text,
	writer integer,
	created_at timestamp with time zone not null default now(),
	updated_at timestamp with time zone not null default now(),
	foreign key (writer) references member(id)
)
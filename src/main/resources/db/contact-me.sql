create table contact_me (
    id serial,
    title varchar(3000),
    email varchar(1500),
    content text,
    confirm bool default false,
    create_at timestamp with time zone not null default now(),
    update_at timestamp with time zone not null default now(),
    constraint pk_contact_me_id primary key (id)
)
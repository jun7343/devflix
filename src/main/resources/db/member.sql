create table "member" (
  id serial,
  status varchar(50) not null,
  email varchar(300) not null,
  username varchar(200) not null,
  password varchar(300) not null,
  authority varchar[] not null default '{}',
  create_at timestamp with time zone not null default now(),
  update_at timestamp with time zone not null default now(),
  constraint pk_member_id primary key (id),
  constraint uk_member_email unique (email)
)
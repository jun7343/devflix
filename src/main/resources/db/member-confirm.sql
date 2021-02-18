create table "member_confirm" (
  id serial,
  status varchar(50) not null,
  email varchar(300) not null,
  code varchar(500) not null,
  create_at timestamp with time zone not null default now(),
  update_at timestamp with time zone not null default now(),
  constraint pk_member_confirm_id primary key (id),
  constraint uk_member_confirm_email unique (email)
)
create table "member_confirm" (
  id serial,
  type varchar(50) not null,
  email varchar(300) not null,
  uuid varchar(500) not null,
  confirm_count int not null default 0,
  create_at timestamp with time zone not null default now(),
  update_at timestamp with time zone not null default now(),
  constraint pk_member_confirm_id primary key (id),
  constraint uk_member_confirm_email unique (email)
)
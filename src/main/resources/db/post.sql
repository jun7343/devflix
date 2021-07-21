create table "post" (
  id serial,
  status varchar(100) not null,
  writer_id int,
  title varchar(5000) not null,
  content text,
  view int,
  dev_post_id int,
  create_at timestamp with time zone not null default now(),
  update_at timestamp with time zone not null default now(),
  constraint pk_post_id primary key(id),
  constraint fk_post_writer_id foreign key (writer_id) references member(id),
  constraint fk_post_dev_post_id foreign key (dev_post_id) references post(id)
)
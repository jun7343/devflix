create table post (
  id serial,
  status varchar(100) not null,
  writer int,
  title varchar(5000) not null,
  content text,
  view int,
  dev_post_url varchar[],
  path_base varchar(1000),
  image_path varchar[],
  create_at timestamp with time zone not null default now(),
  update_at timestamp with time zone not null default now(),
  constraint pk_post_id primary key(id),
  constraint fk_post_writer foreign key (writer) references member(id)
)
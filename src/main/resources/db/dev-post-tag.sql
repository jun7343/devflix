create table "dev_post_tag" (
		id bigserial,
		dev_post_id int,
		tag varchar(100),
		create_at timestamp with time zone not null default now(),
		constraint pk_dev_post_tag_id primary key(id),
		constraint fk_dev_post_tag_dev_post_id foreign key (dev_post_id) key reference dev_post(id)
)
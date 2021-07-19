create table "post_image" (
	id bigserial,
	path_base vharchar(5000),
	image_name varchar(5000),
	post_id int,
	create_at timestamp with time zone default now(),
	constraint pk_post_image primary key(id),
	constraint fk_post_image_post_id foreign key(post_id) references post(id)
)
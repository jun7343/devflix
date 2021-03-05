create table post_dev_post (
    id serial,
    post int,
    dev_post int,
    create_at timestamp with time zone not null default now(),
    update_at timestamp with time zone not null default now(),
    constraint pk_post_dev_post_id primary key(id),
    constraint fk_post_dev_post_post foreign key (post) references post(id),
    constraint fk_post_dev_post_dev_post foreign key (dev_post) references dev_post(id)
)
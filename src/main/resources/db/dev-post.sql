create table "dev_post" (
    id bigserial,
    category varchar(200) not null,
    post_type varchar(200) not null,
    status varchar(200) not null,
    view int not null default 0,
    title varchar(500) not null,
    description text,
    writer varchar(200),
    url varchar(500) not null,
    upload_at timestamp with time zone,
    thumbnail varchar(500),
    create_at timestamp with time zone not null default now(),
    update_at timestamp with time zone not null default now(),
    constraint pk_dev_post_id primary key (id),
    constraint uk_dev_post_url unique (url)
)
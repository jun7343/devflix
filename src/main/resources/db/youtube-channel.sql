create table youtube_channel (
    id serial,
    status varchar(300) not null,
    category varchar(200) not null,
    channel_id varchar(300) not null,
    channel_title varchar(500) not null,
    etag varchar(300),
    description text,
    thumbnail varchar(500),
    publish_at timestamp with time zone not null default now(),
    crawling_at bigint not null default 0,
    create_at timestamp with time zone not null default now(),
    update_at timestamp with time zone not null default now(),
    constraint pk_youtube_channel_id primary key(id),
    constraint uk_youtube_channel_channel_id unique(channel_id),
    constraint uk_youtube_channel_category unique(category)
)
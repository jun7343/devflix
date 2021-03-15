create table dev_blog (
    id serial,
    status varchar(300),
    category varchar(500),
    blog_name varchar(500),
    url varchar(3000),
    thumbnail varchar(2000),
    create_at timestamp with time zone not null default now(),
    update_at timestamp with time zone not null default now(),
    constraint pk_dev_blog_id primary key (id),
    constraint uk_dev_blog_category unique (category),
    constraint uk_dev_blog_url unique (url)
)
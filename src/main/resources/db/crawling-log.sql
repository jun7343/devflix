create table crawling_log (
    id serial,
    job_name varchar(300) not null,
    job_start_at bigint not null,
    job_end_at bigint not null,
    total_crawling int default 0,
    message text,
    success boolean,
    create_at timestamp with time zone not null default now(),
    update_at timestamp with time zone not null default now(),
    constraint pk_crawling_scheduler_log_id primary key (id)
)
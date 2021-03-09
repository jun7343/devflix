create table post_comment (
    id serial,
    status varchar(200) not null,
    writer_id int,
    post_id int,
    comment text,
    create_at timestamp with time zone not null default now(),
    update_at timestamp with time zone not null default now(),
    constraint pk_post_comment_id primary key (id),
    constraint fk_post_comment_writer_id foreign key(writer_id) references member(id),
    constraint fk_post_comment_post_id foreign key(post_id) references post(id)
)
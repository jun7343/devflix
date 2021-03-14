create table post_comment_alert(
    id serial,
    comment_id int,
    user_id int,
    confirm bool not null default false,
    create_at timestamp with time zone not null default now(),
    update_at timestamp with time zone not null default now(),
    constraint pk_post_comment_alert_id primary key (id),
    constraint fk_post_comment_alert_comment_id foreign key (comment_id) references post_comment(id),
    constraint fk_post_comment_alert_user_id foreign key (user_id) references member(id)
)
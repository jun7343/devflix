create table member (
    id  bigserial not null,
    authority varchar[] not null,
    created_at timestamp not null,
    name varchar(255) not null,
    password varchar(255) not null,
    updated_at timestamp not null,
    username varchar(255) not null,
    primary key (id)
)

alter table member add constraint UK_gc3jmn7c2abyo3wf6syln5t2i unique (username)
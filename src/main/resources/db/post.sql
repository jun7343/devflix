create table post (
    id  bigserial not null,
    content varchar(255),
    created_date timestamp,
    title varchar(255),
    updated_date timestamp,
    writer_id int8,
    primary key (id)
)

alter table post add constraint FKh3voybp05rhyyvwlhflfrlti2 foreign key (writer_id) references member
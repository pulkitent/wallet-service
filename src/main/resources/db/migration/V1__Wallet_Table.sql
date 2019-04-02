
    create table wallet (
       id  bigserial not null,
        balance int4 not null,
        name varchar(255),
        primary key (id)
    )

create sequence seq
    increment by 50;

create table user_entity
(
    id            bigint       not null,
    identifier    varchar(36)  not null
        constraint uk_ibvyj8d7oyx5fhmxua32wmygv
            unique,
    created_by    varchar(255),
    created_date  timestamp(6) with time zone,
    modified_by   varchar(255),
    modified_date timestamp(6) with time zone,
    version       bigint,
    password      varchar(255) not null,
    username      varchar(36)  not null
        constraint uk_2jsk4eakd0rmvybo409wgwxuw
            unique,
    constraint user_entity_pkey
        primary key (id, identifier)
);

create table element_entity
(
    type             varchar(31) not null,
    id               bigint      not null
        constraint element_entity_pkey
            primary key,
    created_by       varchar(255),
    created_date     timestamp(6) with time zone,
    modified_by      varchar(255),
    modified_date    timestamp(6) with time zone,
    version          bigint,
    description      varchar(255),
    name             varchar(255),
    path             varchar(255)
        constraint uk_gnja8ur19e9fxpq92ak78xwl4
            unique,
    space_id         bigint
        constraint fk3o90lm15i382q4n9pjy4ncl8t
            references element_entity,
    parent_id        bigint
        constraint fkissq0epjfje1c170e9ffut3sj
            references element_entity,
    owner_id         bigint,
    owner_identifier varchar(36),
    constraint fkpxra4jcx4qa31vusoo78mlclp
        foreign key (owner_id, owner_identifier) references user_entity
);

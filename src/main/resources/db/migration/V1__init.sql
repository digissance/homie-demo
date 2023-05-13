create sequence seq
    increment by 50;

create table user_entity
(
    id            bigint       not null,
    identifier    varchar(36)  not null
        constraint uk_user_entity_identifier
            unique,
    created_by    varchar(255),
    created_date  timestamp(6) with time zone,
    modified_by   varchar(255),
    modified_date timestamp(6) with time zone,
    version       bigint,
    password      varchar(255) not null,
    username      varchar(36)  not null
        constraint uk_user_entity_username
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
        constraint uk_element_entity_path
            unique,
    photo_id         bigint,
    space_id         bigint      not null
        constraint fk_element_entity_space_id
            references element_entity,
    parent_id        bigint
        constraint fk_element_entity_parent_id
            references element_entity,
    owner_id         bigint,
    owner_identifier varchar(36),
    constraint fk_element_entity_owner
        foreign key (owner_id, owner_identifier) references user_entity
);

create table photo_entity
(
    id            bigint not null
        constraint photo_entity_pkey
            primary key,
    created_by    varchar(255),
    created_date  timestamp(6) with time zone,
    modified_by   varchar(255),
    modified_date timestamp(6) with time zone,
    version       bigint,
    image         varchar(255),
    secureurl     varchar(255),
    title         varchar(255),
    element_id    bigint
        constraint uk_photo_entity_element_id
            unique
        constraint fk_photo_entity_element_id
            references element_entity
);

alter table element_entity
    add constraint fk_element_id_photo_id
        foreign key (photo_id) references photo_entity;


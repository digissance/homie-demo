alter table if exists element_entity
    add column from_id bigint;

alter table if exists element_entity
    add constraint fk_element_entity_from_id
        foreign key (from_id)
            references element_entity;
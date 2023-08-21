drop table friend;

create table relation
(
    source_user_id   uuid         NOT NULL REFERENCES user_profile,
    target_user_id   uuid         NOT NULL REFERENCES user_profile,
    relation_type varchar(100) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    primary key (source_user_id, target_user_id, relation_type)
);

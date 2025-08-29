--changeset eyota:category
create table CATEGORY
(
    ID       VARCHAR(255) not null
        primary key,
    FIX_COST BOOLEAN,
    NAME     VARCHAR(255)
)
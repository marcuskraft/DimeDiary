--changeset eyota:bankAccountCategory
create table BANK_ACCOUNT_CATEGORY
(
    ID              VARCHAR(255) not null
        primary key,
    IS_REAL_ACCOUNT BOOLEAN,
    NAME            VARCHAR(255)
)
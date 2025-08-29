--changeset eyota:bankAccount

create table BANK_ACCOUNT
(
    ID                       VARCHAR(255) not null
        primary key,
    BANK_NAME                VARCHAR(255),
    BIC                      VARCHAR(255),
    DATE_START_BALANCE       TIMESTAMP,
    IBAN                     VARCHAR(255),
    NAME                     VARCHAR(255),
    START_BALANCE_EURO_CENT  INTEGER,
    BANK_ACCOUNT_CATEGORY_ID VARCHAR(255)
        constraint FKDLWSW3U8XCVWCC3KHDBGTBT8
            references BANK_ACCOUNT_CATEGORY
)
--changeset eyota:continuousTransaction


create table CONTINUOUS_TRANSACTION
(
    ID               VARCHAR(255) not null
        primary key,
    AMOUNT_EURO_CENT INTEGER,
    DATE_BEGIN       TIMESTAMP,
    FIX_COST         BOOLEAN,
    NAME             VARCHAR(255),
    RECURRENCE_RULE  VARCHAR(255),
    TIMESTAMP        TIMESTAMP,
    BANK_ACCOUNT_ID  VARCHAR(255)
        constraint FKRANCCV14H43JUI9QK8DNWER74
            references BANK_ACCOUNT,
    CATEGORY_ID      VARCHAR(255)
        constraint FKDY3U288OE6OHXS6DBYMXYTHR8
            references CATEGORY
)
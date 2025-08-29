--changeset eyota:balances

create table BALANCES
(
    ID                VARCHAR(255) not null
        primary key,
    BALANCE_EURO_CENT INTEGER,
    DATE              TIMESTAMP,
    BANK_ACCOUNT_ID   VARCHAR(255)
        constraint FKI0O1KKUKL1Y62FEV9KGQCUS1O
            references BANK_ACCOUNT (ID),
    CONSTRAINT user_info UNIQUE (BANK_ACCOUNT_ID, DATE)
)
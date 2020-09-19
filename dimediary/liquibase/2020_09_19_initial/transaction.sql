--changeset eyota:transaction


create table TRANSACTIONS
(
    ID                        VARCHAR(255) not null
        primary key,
    AMOUNT_EURO_CENT          INTEGER,
    DATE                      TIMESTAMP,
    FIX_COST                  BOOLEAN,
    NAME                      VARCHAR(255),
    TIMESTAMP                 TIMESTAMP,
    BANK_ACCOUNT_ID           VARCHAR(255)
        constraint FKHKB7ABV6KD5IT01GL84QNGUD2
            references BANK_ACCOUNT,
    CATEGORY_ID               VARCHAR(255)
        constraint FKJHLVPX4NSOUYKKRC8R9BPVMHI
            references CATEGORY,
    CONTINUOUS_TRANSACTION_ID VARCHAR(255)
        constraint FK40VEGG7KLCX2UQGRXPBDQAILP
            references CONTINUOUS_TRANSACTION
)
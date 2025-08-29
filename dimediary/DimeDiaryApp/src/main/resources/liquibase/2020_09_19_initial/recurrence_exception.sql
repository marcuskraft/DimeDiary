--changeset eyota:recurrence_exception

create table RECURRENCE_EXCEPTION
(
    ID                        VARCHAR(255) not null
        primary key,
    CONTINUOUS_TRANSACTION_ID VARCHAR(255)
        constraint FKDY3U288WEJKF6DBYMXYTHR8
            references CONTINUOUS_TRANSACTION,
    EXCEPTION_DATE            TIMESTAMP
)
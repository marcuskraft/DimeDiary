--changeset eyota:recurrence_extra_instances

create table RECURRENCE_EXTRA_INSTANCE
(
    ID                        VARCHAR(255) not null
        primary key,
    CONTINUOUS_TRANSACTION_ID VARCHAR(255)
        constraint FKDY2U288WEJKF6DBYMXYTHR8
            references CONTINUOUS_TRANSACTION,
    INSTANCE_DATE             TIMESTAMP
)
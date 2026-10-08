CREATE TABLE finance.accounts
(
    account_id     uuid                            DEFAULT gen_random_uuid(),
    account_name   VARCHAR(150)           NOT NULL,
    account_type   finance.account_type   NOT NULL,
    provider_name  VARCHAR(150),
    account_number VARCHAR(50)            NOT NULL,
    currency       VARCHAR(3)             NOT NULL DEFAULT 'INR',
    account_status finance.account_status NOT NULL DEFAULT 'ACTIVE',
    notes          TEXT,
    user_id        VARCHAR(128)           NOT NULL,
    created_at     TIMESTAMPTZ            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version        BIGINT                 NOT NULL DEFAULT 0,

    CONSTRAINT pk_accounts
        PRIMARY KEY (account_id),

    CONSTRAINT uq_account_user_account_number
        UNIQUE (user_id, account_number),

    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES profile.users (user_id)
            ON DELETE CASCADE
)
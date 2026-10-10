CREATE TABLE finance.holdings
(
    holding_id     uuid                   NOT NULL DEFAULT gen_random_uuid(),

    user_id        VARCHAR(128)           NOT NULL,

    holding_name   VARCHAR(150)           NOT NULL,
    holding_type   finance.holding_type   NOT NULL,
    currency       VARCHAR(3)             NOT NULL DEFAULT 'INR',

    holding_status finance.holding_status NOT NULL DEFAULT 'ACTIVE',
    notes          TEXT,

    created_at     TIMESTAMPTZ            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version        BIGINT                 NOT NULL DEFAULT 0,

    CONSTRAINT pk_holdings
        PRIMARY KEY (holding_id),

    CONSTRAINT fk_holding_user_id
        FOREIGN KEY (user_id)
            REFERENCES profile.users (user_id)
            ON DELETE CASCADE,

    CONSTRAINT uq_holdings_holding_user
        UNIQUE (holding_id, user_id)
);

CREATE INDEX idx_holdings_user_type_status
    ON finance.holdings (user_id, holding_type, holding_status);
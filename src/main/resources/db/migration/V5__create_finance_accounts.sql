-- Tables for finance
CREATE TABLE finance.accounts
(
    account_id     uuid                            DEFAULT gen_random_uuid(),
    account_name   VARCHAR(150)           NOT NULL,
    account_type   finance.account_type   NOT NULL,
    provider_name  VARCHAR(150),
    account_number VARCHAR(50),
    currency       VARCHAR(3)             NOT NULL DEFAULT 'INR',
    account_status finance.account_status NOT NULL DEFAULT 'ACTIVE',
    notes          TEXT,
    user_id        VARCHAR(128)           NOT NULL,
    created_at     TIMESTAMPTZ            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version        BIGINT                 NOT NULL DEFAULT 0,

    CONSTRAINT pk_accounts
        PRIMARY KEY (account_id),

    CONSTRAINT uq_accounts_account_id_user_id
        UNIQUE (account_id, user_id),

    CONSTRAINT fk_accounts_user_id
        FOREIGN KEY (user_id)
            REFERENCES profile.users (user_id)
            ON DELETE CASCADE
);

CREATE TABLE finance.holdings
(
    holding_id     uuid                            DEFAULT gen_random_uuid(),
    holding_name   VARCHAR(150)           NOT NULL,
    holding_type   finance.holding_type   NOT NULL,
    quantity       NUMERIC(30, 10)        NOT NULL DEFAULT 0,
    average_cost   NUMERIC(20, 8)         NOT NULL DEFAULT 0,
    cost_basis     NUMERIC(20, 4)         NOT NULL DEFAULT 0,
    currency       VARCHAR(3)             NOT NULL DEFAULT 'INR',
    holding_status finance.holding_status NOT NULL DEFAULT 'ACTIVE',
    notes          TEXT,
    user_id        VARCHAR(128)           NOT NULL,
    account_id     uuid                   NOT NULL,
    created_at     TIMESTAMPTZ            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version        BIGINT                 NOT NULL DEFAULT 0,

    CONSTRAINT pk_holdings
        PRIMARY KEY (holding_id),

    CONSTRAINT uq_holding_user_account_holding_name
        UNIQUE (user_id, account_id, holding_name),

    CONSTRAINT fk_holding_user_id
        FOREIGN KEY (user_id)
            REFERENCES profile.users (user_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_holding_account_id_user_id
        FOREIGN KEY (account_id, user_id)
            REFERENCES finance.accounts (account_id, user_id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_holding_quantity_non_negative
        CHECK (quantity >= 0),

    CONSTRAINT chk_holding_average_cost_non_negative
        CHECK (average_cost >= 0),

    CONSTRAINT chk_holding_cost_basis_non_negative
        CHECK (cost_basis >= 0)
);

-- Indexes for finance
CREATE INDEX idx_accounts_user_id
    ON finance.accounts (user_id);

CREATE INDEX idx_holdings_account_id_user_id
    ON finance.holdings (account_id, user_id);
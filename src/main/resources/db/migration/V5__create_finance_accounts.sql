-- Tables for finance
CREATE TABLE finance.accounts
(
    account_id     uuid                   NOT NULL DEFAULT gen_random_uuid(),
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

CREATE TABLE finance.holding_positions
(
    position_id    UUID                   NOT NULL DEFAULT gen_random_uuid(),

    user_id        VARCHAR(128)           NOT NULL,
    holding_id     UUID                   NOT NULL,
    account_id     UUID                   NOT NULL,

    position_name  VARCHAR(200),
    folio_number   VARCHAR(100),

    quantity       NUMERIC(30, 10)        NOT NULL DEFAULT 0,
    average_cost   NUMERIC(20, 8)         NOT NULL DEFAULT 0,
    cost_basis     NUMERIC(20, 4)         NOT NULL DEFAULT 0,

    holding_status finance.holding_status NOT NULL DEFAULT 'ACTIVE',
    notes          TEXT,

    created_at     TIMESTAMPTZ            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version        BIGINT                 NOT NULL DEFAULT 0,

    CONSTRAINT pk_positions
        PRIMARY KEY (position_id),

    CONSTRAINT fk_positions_user
        FOREIGN KEY (user_id)
            REFERENCES profile.users (user_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_positions_holding
        FOREIGN KEY (holding_id, user_id)
            REFERENCES finance.holdings (holding_id, user_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_positions_account
        FOREIGN KEY (account_id, user_id)
            REFERENCES finance.accounts (account_id, user_id)
            ON DELETE RESTRICT,

    CONSTRAINT uq_positions_position_user
        UNIQUE (position_id, user_id),

    CONSTRAINT ck_positions_quantity
        CHECK (quantity >= 0),

    CONSTRAINT ck_positions_average_cost
        CHECK (average_cost >= 0),

    CONSTRAINT ck_positions_cost_basis
        CHECK (cost_basis >= 0)
);

CREATE TABLE finance.transactions
(
    transaction_id   UUID                     NOT NULL DEFAULT gen_random_uuid(),

    user_id          VARCHAR(128)             NOT NULL,
    position_id      UUID                     NOT NULL,

    transaction_type finance.transaction_type NOT NULL,
    transaction_date TIMESTAMPTZ              NOT NULL,

    quantity         NUMERIC(30, 10),
    unit_price       NUMERIC(20, 8),
    total_amount     NUMERIC(20, 4)           NOT NULL DEFAULT 0,
    fees             NUMERIC(20, 4)           NOT NULL DEFAULT 0,
    taxes            NUMERIC(20, 4)           NOT NULL DEFAULT 0,

    currency         VARCHAR(3)               NOT NULL DEFAULT 'INR',
    reference_number VARCHAR(50),
    notes            TEXT,

    created_at       TIMESTAMPTZ              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version          BIGINT                   NOT NULL DEFAULT 0,

    CONSTRAINT pk_transactions
        PRIMARY KEY (transaction_id),

    CONSTRAINT fk_transactions_user_id
        FOREIGN KEY (user_id)
            REFERENCES profile.users (user_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_transactions_position_id
        FOREIGN KEY (position_id, user_id)
            REFERENCES finance.holding_positions (position_id, user_id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_transactions_amount
        CHECK (total_amount >= 0),

    CONSTRAINT chk_transactions_fees
        CHECK (fees >= 0),

    CONSTRAINT chk_transactions_taxes
        CHECK (taxes >= 0),

    CONSTRAINT chk_transactions_quantity
        CHECK (quantity IS NULL OR quantity >= 0),

    CONSTRAINT chk_transactions_unit_price
        CHECK (unit_price IS NULL OR unit_price >= 0)
);

CREATE TABLE finance.investment_details
(
    position_id     UUID        NOT NULL PRIMARY KEY,

    issuer_name     VARCHAR(150),
    instrument_code VARCHAR(100),
    isin            VARCHAR(12),
    exchange        VARCHAR(50),

    interest_rate   NUMERIC(7, 4),
    investment_date DATE,
    maturity_date   DATE,
    maturity_amount NUMERIC(20, 4),

    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version         BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT fk_investment_details_position
        FOREIGN KEY (position_id)
            REFERENCES finance.holding_positions (position_id)
            ON DELETE CASCADE,

    CONSTRAINT ck_investment_details_interest_rate
        CHECK (interest_rate IS NULL OR interest_rate >= 0),

    CONSTRAINT ck_investment_details_maturity_amount
        CHECK (maturity_amount IS NULL OR maturity_amount >= 0),

    CONSTRAINT ck_investment_details_dates
        CHECK (
            investment_date IS NULL
                OR maturity_date IS NULL
                OR maturity_date >= investment_date
            )
);

-- Indexes for finance
CREATE INDEX idx_accounts_user_id
    ON finance.accounts (user_id);

CREATE INDEX idx_holdings_user_type_status
    ON finance.holdings (user_id, holding_type, holding_status);

CREATE INDEX idx_positions_user_holding
    ON finance.holding_positions (user_id, holding_id);

CREATE INDEX idx_positions_account_user
    ON finance.holding_positions (account_id, user_id);

CREATE UNIQUE INDEX uq_positions_holding_account_folio
    ON finance.holding_positions (user_id, holding_id, account_id, folio_number)
    WHERE folio_number IS NOT NULL;

CREATE INDEX idx_transactions_user_date
    ON finance.transactions (user_id, transaction_date DESC);

CREATE INDEX idx_transactions_position_date
    ON finance.transactions (position_id, user_id, transaction_date DESC);
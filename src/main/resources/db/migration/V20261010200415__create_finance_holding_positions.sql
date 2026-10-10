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

CREATE INDEX idx_positions_user_holding
    ON finance.holding_positions (user_id, holding_id);

CREATE INDEX idx_positions_account_user
    ON finance.holding_positions (account_id, user_id);

CREATE UNIQUE INDEX uq_positions_holding_account_folio
    ON finance.holding_positions (user_id, holding_id, account_id, folio_number)
    WHERE folio_number IS NOT NULL;
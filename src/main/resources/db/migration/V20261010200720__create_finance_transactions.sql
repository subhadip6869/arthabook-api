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

CREATE INDEX idx_transactions_user_date
    ON finance.transactions (user_id, transaction_date DESC);

CREATE INDEX idx_transactions_position_date
    ON finance.transactions (position_id, user_id, transaction_date DESC);
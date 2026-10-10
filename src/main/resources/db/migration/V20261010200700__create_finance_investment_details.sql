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
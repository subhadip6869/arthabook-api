CREATE TABLE profile.financial_profiles
(
    user_id             VARCHAR(128) NOT NULL,
    occupation          profile.occupation,
    annual_income_range profile.annual_income_range,
    risk_appetite       profile.risk_appetite,
    base_currency       VARCHAR(3)   NOT NULL DEFAULT 'INR',

    CONSTRAINT pk_financial_profiles
        PRIMARY KEY (user_id),

    CONSTRAINT fk_financial_profiles_user
        FOREIGN KEY (user_id)
            REFERENCES profile.users (user_id)
            ON DELETE CASCADE
);